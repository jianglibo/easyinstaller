#!/bin/sh
# install-java.tcl \
exec tclsh "$0" ${1+"$@"}

package require yaml

# insert-common-script-here:classpath:scripts/tcl/shared.tcl

set action [lindex $argv 3]
set envdict [readEnvFileJson [lindex $argv 1]]
set rpmFile [file join [dict get $envdict remoteFolder] [dict get $envdict software ftou]]

if {[isInstalled powershell]} {
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
