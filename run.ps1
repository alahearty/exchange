#requires -Version 5.1
$ErrorActionPreference = 'Stop'
$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Path

function Write-Step {
    param([string]$Message)
    Write-Host ""
    Write-Host "==> $Message" -ForegroundColor Cyan
}

function Stop-WithError {
    param([string]$Message)
    Write-Host ""
    Write-Host "ERROR: $Message" -ForegroundColor Red
    exit 1
}

function Require-Command {
    param([string]$Name, [string]$Hint)
    if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
        Stop-WithError "$Name was not found. $Hint"
    }
}

function Start-AppWindow {
    param([string]$Title, [string]$WorkingDirectory, [string]$Command)

    $safeTitle = $Title.Replace("'", "''")
    $safeDirectory = $WorkingDirectory.Replace("'", "''")
    $script = @'
$Host.UI.RawUI.WindowTitle = '__TITLE__'
Set-Location -LiteralPath '__DIRECTORY__'
__COMMAND__
'@
    $script = $script.Replace('__TITLE__', $safeTitle).Replace('__DIRECTORY__', $safeDirectory).Replace('__COMMAND__', $Command)
    $encoded = [Convert]::ToBase64String([Text.Encoding]::Unicode.GetBytes($script))
    Start-Process -FilePath 'powershell.exe' -ArgumentList @(
        '-NoExit',
        '-NoProfile',
        '-ExecutionPolicy', 'Bypass',
        '-EncodedCommand', $encoded
    ) | Out-Null
}

Write-Host "Alo Exchange launcher" -ForegroundColor Green
Write-Host "Repository: $repoRoot"

Require-Command 'java' 'Install JDK 21 or newer and reopen this window.'
Require-Command 'mvn' 'Install Maven 3.9 or newer and reopen this window.'

Write-Host ""
Write-Host "Alo Exchange is a server-rendered JSP application; the backend and frontend run in one Jetty process." -ForegroundColor Yellow
Write-Host "PostgreSQL must be running and the 'exchange' database must already be loaded from script.sql."
Write-Host "Demo login: john@doe.com / john.doe"

Write-Step 'Starting Jetty on http://localhost:9999/exchange/'
Start-AppWindow -Title 'Alo Exchange (http://localhost:9999/exchange/)' -WorkingDirectory $repoRoot -Command @'
mvn jetty:run
'@

Write-Host ""
Write-Host "Application: http://localhost:9999/exchange/" -ForegroundColor Yellow
Write-Host "The first Maven run may download dependencies and take a minute."

Start-Sleep -Seconds 12
Start-Process 'http://localhost:9999/exchange/'
