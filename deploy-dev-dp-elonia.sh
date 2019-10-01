#/bin/sh

TIMESTAMP=`date +%Y%m%d.%H%M%S`
BASEDIR=/opt/tomcat/webapps
FILENAME=toop-demo-ui-dp-0.10.5-SNAPSHOT.war
APP=$(pwd)/toop-demo-ui-dp/target/$FILENAME
BACKUPDIR=/toop-dir/backups

if ! [ -x "$(command -v mvn)" ]; then
  echo 'Error: Maven is not installed.' >&2
  exit 1
fi

echo Making App
cd toop-demo-ui-dp
mvn clean package
cd ..

if [ -f ~/$FILENAME ]
  then
    echo stopping Tomcat
    sudo service tomcat stop

    echo Cleaning exisiting dirs
    cd $BASEDIR
    [ ! -d "$BACKUPDIR" ] && mkdir -p "$BACKUPDIR"
    sudo mv ROOT/ $BACKUPDIR/dp.$TIMESTAMP
    sudo chmod 777 $BACKUPDIR/dp.$TIMESTAMP

    echo ROOT
    APPDIR=$BASEDIR/ROOT
    [ ! -d $APPDIR ] && sudo mkdir $APPDIR
    sudo cp $APP $APPDIR
    cd $APPDIR
    sudo unzip -q $FILENAME && sudo rm $FILENAME
    sudo rm $APPDIR/WEB-INF/classes/private-*.properties
    sudo rm $APPDIR/WEB-INF/classes/dcui.properties
    sudo mv $APPDIR/WEB-INF/classes/dcui.elonia-dev.properties $APPDIR/WEB-INF/classes/dcui.properties
    sudo rm $APPDIR/WEB-INF/classes/toop-interface.properties
    sudo mv $APPDIR/WEB-INF/classes/toop-interface.elonia-dev.properties $APPDIR/WEB-INF/classes/toop-interface.properties
    sudo mv $APPDIR/WEB-INF/classes/datasets.elonia.xml $APPDIR/WEB-INF/classes/datasets.xml

    echo starting Tomcat 
    sudo service tomcat start
    echo Done!
  else
    echo "Source file $APP not existing!"
fi
