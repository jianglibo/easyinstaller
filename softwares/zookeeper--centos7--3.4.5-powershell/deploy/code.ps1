# how to run this script. powershell -File /path/to/this/file.
# ParamTest.ps1 - Show some parameter features
# Param statement must be first non-comment, non-blank line in the script
<#
Param(
    [parameter(Mandatory=$true)]
    [alias("e")]
    $EnvironmentName,
    [parameter(Mandatory=$true)]
    [alias("d")]
    $Destination,
    [alias("u")]
    $UserName,
    [alias("p")]
    $Password)
#>
Param(
    [parameter(Mandatory=$true)]
    $envfile
)

$envForExec = Get-Content $envfile | ConvertFrom-Json

<#
tar (child): ./zookeeper-3.4.9.tar.gz1: Cannot open: No such file or directory
tar (child): Error is not recoverable: exiting now
/usr/bin/tar: Child returned status 2
/usr/bin/tar: Error is not recoverable: exiting now

zookeeper-3.4.9/zookeeper-3.4.9.jar.md5
/usr/bin/tar: zookeeper-3.4.9/zookeeper-3.4.9.jar.md5: Cannot open: File exists
/usr/bin/tar: zookeeper-3.4.9: Cannot utime: Operation not permitted
/usr/bin/tar: Exiting with failure status due to previous errors

https://blogs.technet.microsoft.com/heyscriptingguy/2014/03/30/understanding-streams-redirection-and-write-host-in-powershell/
#>
function Run-Tar {
 Param
     (
       [parameter(Position=0, Mandatory=$True)]
       [String]
       $TgzFileName,
       [parameter(Mandatory=$False)]
       [String]
       $DestFolder
    )

#     Remove-Item /opt/vvvvv/* -Recurse -Force

    if ($DestFolder) { # had destFolder parameter
        if (!(Test-Path $DestFolder)) { # if not exists.
#            if ((Get-Item $DestFolder).PSIsContainer) {
            New-Item $DestFolder -ItemType Directory | Out-Null
        }
    }
    $command = "tar -zxvf $TgzFileName $(if ($DestFolder) {`" -C $DestFolder`"} else {''}) *>&1"
    $r = $command | Invoke-Expression | Where-Object {$_ -cmatch "Cannot open:.*"} | measure
    if ($r.Count -gt 0) {$false} else {$True}
}

<#
<!-- yml->json -->
zkports: "2888,3888"
configFile: "/var/zoo.cfg"
binDir: "/opt/zookeeper"
zkconfig:
  tickTime: "1999"
  dataDir: "/var/lib/zookeeper/"
  clientPort: "2181"
  initLimit: "5"
  syncLimit: "2"

  /var/zoo.cfg's content:
  tickTime=1999
  dataDir=/var/lib/zookeeper/
  clientPort=2181
  initLimit=5
  syncLimit=2
  server.1=zoo1:2888:3888
  server.2=zoo2:2888:3888
  server.3=zoo3:2888:3888
#>

$softwareConfig = ConvertFrom-Json $envForExec.software.configContent

$tgzFile = Join-Path  -Path $envForExec.remoteFolder -ChildPath $envForExec.software.filesToUpload[0].Split('/')[-1]

$zkconfigLines = $softwareConfig.zkconfig.psobject.Properties | where MemberType -eq "NoteProperty" | foreach {"$($_.name)=$($_.value)"}
# $srvlines = $envForExec.boxGroup.boxes | Select-Object @{n="serverId"; e={$_.ip -split '.',0,"simplematch" | Select-Object -Last 1}}, hostname| ForEach-Object {"server.$($_.serverId)=$($_.hostname):$($softwareConfig.zkports)"}

$srvlines = $envForExec.boxGroup.boxes | Select-Object @{n="serverId"; e={$_.ip.split('\.')[-1]}}, hostname | ForEach-Object {"server.{0}={1}:{2}:{3}" -f (@($_.serverId, $_.hostname) + $softwareConfig.zkports.Split(','))}

$configFileFolder = Split-Path -Parent $softwareConfig.configFile

if (!(Test-Path $configFileFolder)) {
    New-Item -Path $configFileFolder -ItemType Directory | Out-Null
}

$dataDir = $softwareConfig.zkconfig.dataDir
if (!(Test-Path $dataDir)) {
    New-Item -Path $dataDir -ItemType Directory | Out-Null
}

$zkconfigLines + $srvlines | Out-File $softwareConfig.configFile

if (Test-Path $tgzFile) {
    Run-Tar $tgzFile -DestFolder $softwareConfig.binDir
}

# $zkconfig.psobject.Properties | where MemberType -eq "NoteProperty" | foreach -Begin {$h = @{}} -Process {$h.Set_Item($_.Name, $_.Value)} -End {$h} #Get hash table.
#$softwareConfig | gm -MemberType NoteProperty | Select-Object -Property Name
#$softwareConfig.psobject.Properties | where MemberType -eq "NoteProperty" | where Name -NE zkconfig|measure

<#
function positional {
 Param
     (
       [parameter(Position=0, Mandatory=$True)]
       [String[]]
       $ComputerName
    ) 
}
#>