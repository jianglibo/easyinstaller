#!/bin/sh
# install-java.tcl \
exec tclsh "$0" ${1+"$@"}

package require yaml

proc loadYaml {fileName} {
  if {[catch {set dt [::yaml::yaml2dict -file $fileName]} msg o]} {
    puts $msg
    exit 1
  } else {
    return $dt
  }
}

set envfile [lindex $argv 1]
set action [lindex $argv 2]

set envdict [loadYaml $envfile]

foreach box [dict get $envdict boxGroup boxes] {
  puts $box
  set ips [dict get $box commaSepPorts]
  puts "server.[lindex [split [dict get $box ip] .] end]=[dict get $box hostname]:"
  puts ---------------------------
}

# F:\github\easyinstaller\softwares\zookeeper--centos7--3.4.9>tclsh code.tcl --envfile ../envforcodeexec.yaml
#server.1=zoo1:2888:3888
#server.2=zoo2:2888:3888
#server.3=zoo3:2888:3888

set d [::yaml::yaml2dict -stream "hello: abc\nbbb: ccc\nports: 555.666"]

dict for {k v} $d {
  if {{ports} ne $k} {
    puts "$k=$v"
  }
}
