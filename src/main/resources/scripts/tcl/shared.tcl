proc loadYaml {fileName} {
  if {[catch {set dt [::yaml::yaml2dict -file $fileName]} msg o]} {
    puts $msg
    exit 1
  } else {
    return $dt
  }
}

proc readEnvFileJson {fileName} {
  set jd [::json::json2dict [readWholeFile $fileName]]
  set bgConfigContent [dict get $jd boxGroup configContent]
  dict set jd boxGroup configContent [::json::json2dict $bgConfigContent]
  set sfConfigContent [dict get $jd software configContent]
  dict set jd software configContent [::json::json2dict $sfConfigContent]

  set ftou {}
  foreach f [dict get $jd software filesToUpload] {
    lappend ftou [lindex [split $f /] end]
  }
  dict set jd software ftou $ftou
  return $jd
}

proc readWholeFile {fileName} {
  if {[catch {open $fileName} fid o]} {
    puts $fid
    exit 1
  } else {
    set data [read $fid]
    close $fid
  }
  return $data
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

proc isInstalled {execName} {
  catch {exec which $execName} msg o
  if {[dict get $o -code] == 0} {
    return 1
  }
  return 0
}
