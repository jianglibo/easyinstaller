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

function code {
   
}

Write-Output $envfile

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