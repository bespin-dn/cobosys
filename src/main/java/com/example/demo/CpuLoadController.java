package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Controller
public class CpuLoadController {

    private boolean cpuLoadRunning = false;
    private Thread cpuLoadThread = null; // CPU 부하 생성 스레드 참조를 위한 필드 추가

    @GetMapping("/")
    public String index(Model model) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            model.addAttribute("ipAddress", ip.getHostAddress());

            double cpuUsage = getSystemCpuUsage();
            model.addAttribute("cpuUsage", cpuUsage);

        } catch (Exception e) {
            model.addAttribute("error", "오류가 발생했습니다: " + e.getMessage());
        }

        model.addAttribute("cpuLoadRunning", cpuLoadRunning);
        return "index";
    }

    @PostMapping("/start-cpu-load")
    public String startCpuLoad() {
        if (cpuLoadRunning == false) { // 이미 실행 중이지 않은 경우에만 새 스레드 시작
            cpuLoadRunning = true;
            cpuLoadThread = new Thread(this::generateCpuLoad);
            cpuLoadThread.start(); // CPU 부하 생성을 위한 쓰레드 시작
        }
        return "redirect:/";
    }

    @PostMapping("/stop-cpu-load")
    public String stopCpuLoad() {
        cpuLoadRunning = false;
        if (cpuLoadThread != null) {
            try {
                cpuLoadThread.join(1000); // 스레드가 종료될 때까지 최대 1초 기다림
                if (cpuLoadThread.isAlive()) {
                    cpuLoadThread.interrupt(); // 스레드가 여전히 실행 중이면 인터럽트 시도
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 현재 스레드에 대한 인터럽트 상태 설정
            }
            cpuLoadThread = null; // 참조 제거
        }
        return "redirect:/";
    }

    private void generateCpuLoad() {
        while (cpuLoadRunning && !Thread.currentThread().isInterrupted()) {
            double x = 0.0001;
            for (int i = 0; i <= 1000000; i++) {
                x += Math.sqrt(x);
            }
        }
    }
    
    // CPU 사용률을 가져오는 메소드
    private double getSystemCpuUsage() {
        try {
            Process process = Runtime.getRuntime().exec("top -b -n 1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            double usCpuUsage = 0.0;
    
            while ((line = reader.readLine()) != null) {
                if (line.contains("Cpu(s):")) {
                    // 'Cpu(s):' 행을 찾은 후, us 값을 추출
                    String[] parts = line.split(",")[0].trim().split(" "); // ','로 나누고 첫 부분을 공백으로 나눔
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].equals("us")) {
                            // 'us' 바로 앞의 값이 CPU 사용률
                            usCpuUsage = Double.parseDouble(parts[i-1]);
                            return usCpuUsage;
                        }
                    }
                }
            }
            reader.close();
            return usCpuUsage;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0; // 오류 발생 시 0 반환
        }
    }

}
