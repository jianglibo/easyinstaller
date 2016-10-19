#!/bin/sh
# install-java.tcl \
exec tclsh "$0" ${1+"$@"}

package require yaml

# insert-common-script-here:classpath:scripts/tcl/shared.tcl

EnvDictNs::initialize [lindex $argv 1]

set rpmFile [EnvDictNs::getUpload *rpm*]

if {[EnvDictNs::isInstalled powershell]} {
    puts "powershell already installed.@@success@@"
} else {
  if { [catch {exec yum install -y $rpmFile msg o] } {
      if {[string match -nocase "*Nothing to do*" $msg]} {
          puts "already installed @@success@@"
          exit 0
      } else {
          puts $msg
          exit 1
      }
  }
}
puts "@@success@@"
# for test only
