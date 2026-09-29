$auth = [System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes("admin:admin"))
for ($i=1; $i -le 200; $i++) {
    $body = "{
        `"fullName`": `"Test User $i`",
        `"email`": `"testuser$i@example.com`",
        `"phoneNumber`": `"+1987654$($i.ToString('000'))`",
        `"username`": `"testuser$i`",
        `"password`": `"securepassword`",
        `"addresses`": [
            {
                `"houseNo`": `"$i`",
                `"street`": `"Main St`",
                `"area`": `"Downtown`",
                `"village`": `"Central`",
                `"district`": `"Metropolis`",
                `"state`": `"NY`",
                `"country`": `"USA`",
                `"postalCode`": `"10001`",
                `"addressType`": `"HOME`",
                `"latitude`": 40.7128,
                `"longitude`": -74.0060
            }
        ]
    }"
    try {
        Invoke-RestMethod -Uri "http://localhost:8080/api/v1/users" -Method Post -Headers @{Authorization="Basic $auth"} -ContentType "application/json" -Body $body | Out-Null
        if ($i % 20 -eq 0) { Write-Host "Successfully created $i users so far..." }
    } catch {
        Write-Host "Failed to create user $i. Error: $_"
        break
    }
}
Write-Host "Done!"
