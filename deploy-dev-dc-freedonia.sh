#
# Copyright (C) 2018-2019 toop.eu
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#/bin/sh

TIMESTAMP=`date +%Y%m%d.%H%M%S`
BASEDIR=/opt/tomcat/webapps
FILENAME=toop-demo-ui-dc-*-SNAPSHOT.war
APP=$(pwd)/toop-demo-ui-dc/target/$FILENAME
BACKUPDIR=/toop-dir/backups

if ! [ -x "$(command -v mvn)" ]; then
  echo 'Error: Maven is not installed.' >&2
  exit 1
fi
if ! [ -x "$(command -v git)" ]; then
  echo 'Error: Git is not installed.' >&2
  exit 1
fi
if ! [ -x "$(command -v javac)" ]; then
  echo 'Error: JDK is not installed.' >&2
  exit 1
fi

echo Fetching latest changes
git pull

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
    sudo mv $APPDIR/WEB-INF/classes/dcui.freedonia-dev.properties $APPDIR/WEB-INF/classes/dcui.properties
    sudo rm $APPDIR/WEB-INF/classes/toop-interface.properties
    sudo mv $APPDIR/WEB-INF/classes/toop-interface.freedonia-dev.properties $APPDIR/WEB-INF/classes/toop-interface.properties

    echo starting Tomcat 
    sudo service tomcat start
    echo Done!
  else
    echo "Source file $APP not existing!"
fi