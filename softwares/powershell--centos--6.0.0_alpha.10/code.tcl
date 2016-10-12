#!/bin/sh
# install-java.tcl \
exec tclsh "$0" ${1+"$@"}

package require yaml

# ---------- common utils --------------------------
proc loadYaml {fileName} {
  if {[catch {set dt [::yaml::yaml2dict -file $fileName]} msg o]} {
    puts $msg
    exit 1
  } else {
    return $dt
  }
}

proc sethostname {hn} {
  if {[string length $hn] > 0} {
    exec hostnamectl set-hostname $hn --static
  }
}

proc backupOrigin {fn} {
  if {[file exists $fn]} {
    set of "$fn.origin"
    if {! [file exists $of]} {
      exec cp $fn $of
    }
  }
}

proc setupResolver {nameserver} {
  set resolverFile /etc/resolv.conf
  backupOrigin $resolverFile
  if {[catch {open $resolverFile w} fid o]} {
    puts $fid
    exit 1
  } else {
    puts $fid "nameserver $nameserver"
    close $fid
  }
}

proc openFirewall {prot args} {
  foreach port $args {
    catch {exec firewall-cmd --permanent --zone=public --add-port ${port}/$prot} msg o
  }
  catch {exec firewall-cmd --reload} msg o
}
# ---------- common utils --------------------------

set envfile [lindex $argv 1]
set action [lindex $argv 2]
set envdict [loadYaml $envfile]
set remoteFolder [dict get $envdict remoteFolder]

exec yum install -y "${remoteFolder}powershell-6.0.0_alpha.10-1.el7.centos.x86_64.rpm"
