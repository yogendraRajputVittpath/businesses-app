const API_PROFILE = "http://localhost:9090/api/users/get-profile";
const API_PROFILE_PIC = "http://localhost:9090/api/users/profile-pic";

// Get token
const getToken = () => (localStorage.getItem("TOKEN") || "").trim();

// Fetch Profile Info
async function fetchProfile() {
    const token = getToken();
    if (!token) return alert("No token found, please login again.");

    const res = await fetch(API_PROFILE, {
        headers: { "Authorization": "Bearer " + token }
    });
    const data = await res.json();

    if (data.status === "SUCCESS") {
        document.getElementById("fullName").textContent =
            data.data.firstName + " " + data.data.lastName;
        document.getElementById("emailDisplay").textContent = data.data.email;
        document.getElementById("phoneNo").textContent = data.data.phoneNo;
    } else {
        alert("Failed to fetch profile: " + data.message);
    }
}

// Fetch Profile Pic
async function fetchProfilePic() {
    const token = getToken();
    if (!token) return;

    const res = await fetch(API_PROFILE_PIC, {
        headers: { "Authorization": "Bearer " + token }
    });
    const data = await res.json();

    if (data.status === "SUCCESS") {
        document.getElementById("profilePic").src =
            "data:image/jpeg;base64," + data.data;
    }
}

// On page load
window.onload = () => {
    if (!getToken()) {
        alert("Token not found, redirecting to login...");
        window.location.href = "login.html";
        return;
    }

    fetchProfile();
    fetchProfilePic();
};
