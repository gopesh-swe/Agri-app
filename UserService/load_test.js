const duration = 5 * 60 * 1000; // 5 minutes
const concurrency = 30; // 30 concurrent connections to blast the API
let successCount = 0;
let failCount = 0;
let isRunning = true;
const auth = "Basic " + Buffer.from("admin:admin").toString("base64");

console.log(`Starting load test for ${duration / 1000} seconds with ${concurrency} concurrent workers...`);

// Stop after duration
setTimeout(() => {
    isRunning = false;
}, duration);

const start = Date.now();

async function worker(workerId) {
    while (isRunning) {
        // Generate unique fields to avoid database constraint violations
        const uniqueId = Date.now().toString(36) + Math.random().toString(36).substring(2, 6);
        const body = {
            fullName: "Load Test " + uniqueId,
            email: "load" + uniqueId + "@example.com",
            phoneNumber: "+1999" + Math.floor(1000000 + Math.random() * 9000000),
            username: "user" + uniqueId.substring(0, 10),
            password: "securepassword",
            addresses: [
                {
                    houseNo: "123",
                    street: "Test St",
                    area: "Test Area",
                    village: "Test Village",
                    district: "Test District",
                    state: "TS",
                    country: "USA",
                    postalCode: "10000",
                    addressType: "HOME",
                    latitude: 40.0,
                    longitude: -74.0
                }
            ]
        };

        try {
            const res = await fetch("http://localhost:8080/api/v1/users", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": auth
                },
                body: JSON.stringify(body)
            });
            
            // Consume the response body to avoid memory leaks in Node fetch
            await res.text();
            
            if (res.ok) {
                successCount++;
            } else {
                failCount++;
            }
        } catch (e) {
            failCount++;
        }
    }
}

// Start concurrent workers
const workers = [];
for (let i = 0; i < concurrency; i++) {
    workers.push(worker(i));
}

// Print progress every 15 seconds
const interval = setInterval(() => {
    const elapsed = (Date.now() - start) / 1000;
    const reqsPerSec = Math.round((successCount + failCount) / elapsed);
    console.log(`[${Math.round(elapsed)}s] Success: ${successCount} | Failed: ${failCount} | Throughput: ${reqsPerSec} req/s`);
}, 15000);

// Wait for all workers to finish
Promise.all(workers).then(() => {
    clearInterval(interval);
    const totalTime = (Date.now() - start) / 1000;
    console.log("\n=== LOAD TEST FINISHED ===");
    console.log(`Total Time: ${totalTime.toFixed(2)} seconds`);
    console.log(`Total Requests: ${successCount + failCount}`);
    console.log(`Successful: ${successCount}`);
    console.log(`Failed: ${failCount}`);
    console.log(`Average Throughput: ${((successCount + failCount) / totalTime).toFixed(2)} requests/second`);
});
