const duration = 2 * 60 * 1000; // 2 minutes
const concurrency = 30; // 30 concurrent connections
let successCount = 0;
let failCount = 0;
let isRunning = true;
const auth = "Basic " + Buffer.from("admin:admin").toString("base64");

console.log("Fetching user IDs from the database to test against...");

async function startTest() {
    // 1. Get all users
    let users = [];
    try {
        const res = await fetch("http://localhost:8080/api/v1/users", {
            headers: { "Authorization": auth }
        });
        if (!res.ok) throw new Error("Failed to fetch users");
        users = await res.json();
    } catch (e) {
        console.error("Error fetching users:", e);
        return;
    }

    // Find the correct ID field (could be id, userId, or uuid)
    const ids = users.map(u => u.id || u.userId || u.uuid).filter(id => id !== undefined);
    if (ids.length === 0) {
        console.log("No valid user IDs found to test against! Make sure users exist and the ID field is exposed.");
        return;
    }
    
    console.log(`Successfully fetched ${ids.length} users. Starting GET load test for ${duration / 1000} seconds with ${concurrency} workers...`);

    // Stop after duration
    setTimeout(() => {
        isRunning = false;
    }, duration);

    const start = Date.now();

    async function worker() {
        while (isRunning) {
            // Pick a random ID from the fetched list
            const randomId = ids[Math.floor(Math.random() * ids.length)];
            try {
                const res = await fetch(`http://localhost:8080/api/v1/users/${randomId}`, {
                    method: "GET",
                    headers: {
                        "Authorization": auth
                    }
                });
                
                await res.text(); // Consume body
                
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
        workers.push(worker());
    }

    // Print progress every 15 seconds
    const interval = setInterval(() => {
        const elapsed = (Date.now() - start) / 1000;
        const reqsPerSec = Math.round((successCount + failCount) / elapsed);
        console.log(`[${Math.round(elapsed)}s] Success: ${successCount} | Failed: ${failCount} | Throughput: ${reqsPerSec} req/s`);
    }, 15000);

    // Wait for all workers to finish
    await Promise.all(workers);
    clearInterval(interval);
    
    const totalTime = (Date.now() - start) / 1000;
    console.log("\n=== LOAD TEST FINISHED ===");
    console.log(`Total Time: ${totalTime.toFixed(2)} seconds`);
    console.log(`Total Requests: ${successCount + failCount}`);
    console.log(`Successful: ${successCount}`);
    console.log(`Failed: ${failCount}`);
    console.log(`Average Throughput: ${((successCount + failCount) / totalTime).toFixed(2)} requests/second`);
}

startTest();
