Param(
    [string]$hostfile,
    [switch]$delete,
    [string]$items,
    [bool]$writeback=$true
)

# ---insert-items-here---

function Update-HostsItems {
    Param(
    [string]$hostfile,
    [switch]$delete,
    [string]$items,
    [bool]$writeback=$true)
    if (!$hostfile) {
        $hostfile = $env:windir | Join-Path -ChildPath "System32\drivers\etc\hosts"
    }

    [array]$lines = Get-Content $hostfile | ForEach-Object {$_.ToString().trim()}

    [array]$items = $items -split "," | ForEach-Object {$_.ToString().Trim()}

    foreach ($item in $items) {
    [array]$pair = $item -split "\s+"
    if ($delete) {
            $lines = $lines | Where-Object {$_ -notmatch $pair[0].ToString().Trim()}
    } else {
            if ($pair.Count -eq 2) {
                $lines = $lines | Where-Object {$_ -notmatch $pair[0].ToString().Trim()}
            }
    }
    }
    if ($writeback) {
        if ($delete) {
            $lines | Out-File $hostfile -Encoding ascii
        } else {
            $lines + $items | Out-File $hostfile -Encoding ascii
        }
    } else {
        if ($delete) {
            $lines
        } else {
            $lines + $items
        }
    }
}
if ($items) {
    if ($delete) {
        Update-HostsItems -hostfile $hostfile -items $items -writeback $writeback -delete
    } else {
        Update-HostsItems -hostfile $hostfile -items $items -writeback $writeback
    }
}
