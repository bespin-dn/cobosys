<!-- src/main/webapp/WEB-INF/views/index.jsp -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CPU Load Generator</title>
</head>
<body>
    <h1>CPU Load Generator</h1>
    <p>Internal IP Address: ${ipAddress}</p> <!-- 내부 IP 주소 표시 -->
    <p>CPU Load Running: ${cpuLoadRunning}</p> <!-- CPU 부하 상태 표시 -->
    <p>User space CPU Usage: ${cpuUsage}%</p> <!-- 현재 CPUI 사용량 표시 -->
    
    <form action="/start-cpu-load" method="post">
        <button type="submit">Start CPU Load</button>
    </form>
    <form action="/stop-cpu-load" method="post">
        <button type="submit">Stop CPU Load</button>
    </form>
    
    <!-- 에러 메시지 표시 (옵션) -->
    <c:if test="${not empty error}">
        <p>Error: ${error}</p>
    </c:if>
    <h1>2024-04-08 V1</h1>
    <h2>10:01</h2>
</body>
</html>