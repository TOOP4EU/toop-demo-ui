package eu.toop.demoui.endpoints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;

import eu.toop.commons.concept.EConceptType;
import eu.toop.commons.dataexchange.v140.TDEConceptRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.error.EToopErrorCode;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.demoui.datasets.DPDataset;
import eu.toop.demoui.datasets.DPUIDatasets;
import eu.toop.kafkaclient.ToopKafkaClient;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

final class HandlerDataRequest
{
  private HandlerDataRequest ()
  {}

  private static boolean _canUseConcept (@Nonnull final TDEConceptRequestType aConcept)
  {
    // This class can only deliver to:
    // - "DP" concept types
    // - leaf entries
    // - that don't have a response yet
    return EConceptType.DP.getID ().equals (aConcept.getConceptTypeCode ().getValue ()) &&
           aConcept.hasNoConceptRequestEntries () &&
           aConcept.getDataElementResponseValueCount () == 0;
  }

  private static void _setError (@Nonnull final String sLogPrefix,
                                 @Nonnull final TDEDataElementResponseValueType aValue,
                                 @Nonnull @Nonempty final String sErrorMsg)
  {
    aValue.setErrorIndicator (ToopXSDHelper140.createIndicator (true));
    // Either error code or description
    if (false)
      aValue.setErrorCode (ToopXSDHelper140.createCode (EToopErrorCode.GEN.getID ()));
    else
      aValue.setResponseDescription (ToopXSDHelper140.createText ("MockError from DemoDP: " + sErrorMsg));
    ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "MockError from DemoDP: " + sErrorMsg);
  }

  @Nonnull
  private static EChange _applyStaticDataset (@Nonnull final String sLogPrefix,
                                              @Nonnull final TDEConceptRequestType aConcept,
                                              @Nullable final DPDataset aDataset)
  {
    final EChange eChange;

    final TDEDataElementResponseValueType aValue = new TDEDataElementResponseValueType ();
    aValue.setErrorIndicator (ToopXSDHelper140.createIndicator (false));
    aValue.setAlternativeResponseIndicator (ToopXSDHelper140.createIndicator (false));

    final TextType aConceptName = aConcept.getConceptName ();
    if (aConceptName == null || StringHelper.hasNoText (aConceptName.getValue ()))
    {
      _setError (sLogPrefix, aValue, "Concept name is missing in request: " + aConcept);
      eChange = EChange.CHANGED;
    }
    else
    {
      if (aDataset == null)
      {
        _setError (sLogPrefix, aValue, "No DP dataset found");
        eChange = EChange.CHANGED;
      }
      else
      {
        final String sConceptName = aConceptName.getValue ();
        final String sConceptValue = aDataset.getConceptValue (sConceptName);
        if (sConceptValue == null)
        {
          // No such entry in mapping
          if (false)
            _setError (sLogPrefix, aValue, "Concept [" + sConceptName + "] is missing in DP dataset");
          else
            eChange = EChange.UNCHANGED;
        }
        else
        {
          aValue.setResponseDescription (ToopXSDHelper140.createText (sConceptValue));

          ToopKafkaClient.send (EErrorLevel.INFO,
                                () -> sLogPrefix + "Populated concept [" + sConceptName + "]: [" + sConceptValue + "]");
          eChange = EChange.CHANGED;
        }
      }
    }

    if (eChange.isChanged ())
      aConcept.addDataElementResponseValue (aValue);
    return eChange;
  }

  private static void _applyConceptValues (@Nonnull final TDEDataElementRequestType aDER,
                                           final String sLogPrefix,
                                           final DPDataset dataset)
  {
    final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();
    if (aFirstLevelConcept != null)
    {
      boolean bDidApplyResponse = false;
      if (_canUseConcept (aFirstLevelConcept))
      {
        // Apply on first level - highly unlikely but who knows....
        _applyStaticDataset (sLogPrefix, aFirstLevelConcept, dataset);
        bDidApplyResponse = true;
      }
      else
        second: for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ())
        {
          if (_canUseConcept (aSecondLevelConcept))
          {
            // Apply on second level - used if directly started with TC concepts
            _applyStaticDataset (sLogPrefix, aSecondLevelConcept, dataset);
            bDidApplyResponse = true;
            break second;
          }
          for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ())
          {
            if (_canUseConcept (aThirdLevelConcept))
            {
              // Apply on third level
              _applyStaticDataset (sLogPrefix, aThirdLevelConcept, dataset);
              bDidApplyResponse = true;
              break second;
            }
            // 3 level nesting is maximum
          }
        }

      if (!bDidApplyResponse)
      {
        ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Found no place to provide response value in Data");
      }
    }
  }

  public static void handle (@Nonnull final String sLogPrefix,
                             @Nonnull final TDETOOPRequestType aRequest,
                             @Nonnull final TDETOOPResponseType aResponse)
  {
    // Try to find dataset for natural person
    final TDEDataRequestSubjectType ds = aRequest.getDataRequestSubject ();
    final String naturalPersonIdentifier;
    final String legalEntityIdentifier;

    if (ds.getNaturalPerson () != null &&
        ds.getNaturalPerson ().getPersonIdentifier () != null &&
        StringHelper.hasText (ds.getNaturalPerson ().getPersonIdentifier ().getValue ()))
    {
      naturalPersonIdentifier = ds.getNaturalPerson ().getPersonIdentifier ().getValue ();
      ToopKafkaClient.send (EErrorLevel.INFO,
                            () -> sLogPrefix + "Record matching NaturalPerson: " + naturalPersonIdentifier);
    }
    else
      naturalPersonIdentifier = null;

    if (ds.getLegalPerson () != null &&
        ds.getLegalPerson ().getLegalPersonUniqueIdentifier () != null &&
        StringHelper.hasText (ds.getLegalPerson ().getLegalPersonUniqueIdentifier ().getValue ()))
    {
      legalEntityIdentifier = ds.getLegalPerson ().getLegalPersonUniqueIdentifier ().getValue ();
      ToopKafkaClient.send (EErrorLevel.INFO,
                            () -> sLogPrefix + "Record matching LegalPerson: " + legalEntityIdentifier);
    }
    else
      legalEntityIdentifier = null;

    final ICommonsList <DPDataset> datasets = DPUIDatasets.INSTANCE.getDatasetsByIdentifier (naturalPersonIdentifier,
                                                                                             legalEntityIdentifier);
    final DPDataset dataset = datasets.getFirst ();
    if (dataset != null)
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Dataset found");
    else
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "No dataset found");

    // add all the mapped values in the response
    for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ())
    {
      _applyConceptValues (aDER, sLogPrefix, dataset);
    }
  }
}
