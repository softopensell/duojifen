#!/bin/bash
#定时拆分日志 chmod +x ./tomcatCatalina.sh   15 0 * * * root /root/back/tomcatCatalina.sh
tomcat_location=/root/tomcats/tomcat_xqs  #tomcat 文件路径
cd $tomcat_location/logs
d=`date +%Y%m%d`
d7=`date -d'7 day ago' +%Y%m%d`

cp catalina.out catalina.out.${d}
echo "" > catalina.out 
rm -rf catalina.out.${d7}
