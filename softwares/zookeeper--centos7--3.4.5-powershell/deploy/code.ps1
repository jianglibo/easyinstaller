# how to run this script. powershell -File /path/to/this/file.
# ParamTest.ps1 - Show some parameter features
# Param statement must be first non-comment, non-blank line in the script
Param(
    [parameter(Mandatory=$true)]
    $envfile,
    [parameter(Mandatory=$true)]
    $action
)

$envForExec = Get-Content $envfile | ConvertFrom-Json

# insert-common-script-here:classpath:scripts/powershell/PsCommon.ps1
# Remove-Item /opt/vvvvv/* -Recurse -Force

$softwareConfig = ConvertFrom-Json $envForExec.software.configContent

switch ($action) {
    "install" {
        $tgzFile = Join-Path  -Path $envForExec.remoteFolder -ChildPath $envForExec.software.filesToUpload[0].Split('/')[-1]
        $zkconfigLines = $softwareConfig.zkconfig.psobject.Properties | where MemberType -eq "NoteProperty" | foreach {"{0}={1}" -f $_.name,$_.value}
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
    }
}

"@@success@@"