const API_LOGIN = "http://localhost:9090/api/users/login";

// Login form
document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("emailInput").value;
    const password = document.getElementById("passwordInput").value;

    try {
        const res = await fetch(API_LOGIN, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const data = await res.json();
        console.log("Login Response:", data);

        if (data.status === "SUCCESS") {
            // Save token in localStorage
            //localStorage.setItem("TOKEN", data.data.token.trim());
            //alert("Login Successful!");

			//const token = data.data.token.trim();
			// Remove "Bearer " if present
			let token = data.data.token.trim().replace("Bearer ", "");

			    // Save token in localStorage
			    localStorage.setItem("TOKEN", token);

			    // Show alert with token
			    alert("Login Successful!\n\nYour Token:\n" + token);
			
            // Redirect to profile page
            window.location.href = "profile.html";
        } else {
            alert("Login Failed: " + data.message);
        }
    } catch (err) {
        console.error("Login Error:", err);
    }
});
