#/bin/sh

TIMESTAMP=`date +%Y%m%d.%H%M%S`
BASEDIR=/opt/tomcat/webapps
FILENAME=toop-demo-ui-dc-0.10.5-SNAPSHOT.war
APP=$(pwd)/toop-demo-ui-dc/target/$FILENAME
BACKUPDIR=/toop-dir/backups

if ! [ -x "$(command -v mvn)" ]; then
  echo 'Error: Maven is not installed.' >&2
  exit 1
fi

echo Making App
cd toop-demo-ui-dc
mvn clean package
cd ..

if [ -f $APP ]
  then
    echo stopping Tomcat
    sudo service tomcat stop

    echo Cleaning exisiting dirs
    cd $BASEDIR
    [ ! -d "$BACKUPDIR" ] && mkdir -p "$BACKUPDIR"
    sudo mv ROOT/ $BACKUPDIR/dc.$TIMESTAMP
    sudo chmod 777 $BACKUPDIR/dc.$TIMESTAMP

    echo ROOT
    APPDIR=$BASEDIR/ROOT
    [ ! -d $APPDIR ] && sudo mkdir $APPDIR
    sudo cp $APP $APPDIR
    cd $APPDIR
    sudo unzip -q $FILENAME && sudo rm $FILENAME
    sudo rm $APPDIR/WEB-INF/classes/private-*.properties
    sudo rm $APPDIR/WEB-INF/classes/dcui.properties
    sudo mv $APPDIR/WEB-INF/classes/dcui.freedonia-acc.properties $APPDIR/WEB-INF/classes/dcui.properties
    sudo rm $APPDIR/WEB-INF/classes/toop-interface.properties
    sudo mv $APPDIR/WEB-INF/classes/toop-interface.freedonia-acc.properties $APPDIR/WEB-INF/classes/toop-interface.properties

    echo starting Tomcat 
    sudo service tomcat start
    echo Done!
  else
    echo "Source file $APP not existing!"
fi