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

$envForExec = Get-Content (Join-Path -Path (Split-Path -Path $here -Parent) -ChildPath test\envforcodeexec.json) | ConvertFrom-Json

$softwareConfig = ConvertFrom-Json $envForExec.software.configContent

$zkconfig = $softwareConfig.zkconfig

$zkconfigLines = $zkconfig.psobject.Properties | where MemberType -eq "NoteProperty" | foreach {"$($_.name)=$($_.value)"}

#$softwareConfig | gm -MemberType NoteProperty | Select-Object -Property Name
#$softwareConfig.psobject.Properties | where MemberType -eq "NoteProperty" | where Name -NE zkconfig|measure


function code {
   
}

#Write-Output $envfile

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