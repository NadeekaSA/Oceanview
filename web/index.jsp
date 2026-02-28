<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | Ocean View Resort</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="login-container">
        <h2>Ocean View Resort</h2>
        <p style="text-align: center; color: #666; margin-bottom: 2rem;">Staff Management Portal</p>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form action="login" method="post" onsubmit="return validateLogin()">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn" style="width: 100%;">Login to Dashboard</button>
        </form>
    </div>

    <script>
        function validateLogin() {
            const user = document.getElementById('username').value;
            if (user.length < 3) {
                alert('Username must be at least 3 characters');
                return false;
            }
            return true;
        }
    </script>
</body>
</html>
