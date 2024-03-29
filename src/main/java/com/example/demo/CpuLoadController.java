package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import java.net.InetAddress;

@Controller
public class CpuLoadController {

    private boolean cpuLoadRunning = false;
    private Thread cpuLoadThread = null; // CPU 부하 생성 스레드 참조를 위한 필드 추가

    @GetMapping("/")
    public String index(Model model) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            String ipAddress = ip.getHostAddress();
            model.addAttribute("ipAddress", ipAddress); // IP 주소를 모델에 추가
        } catch (Exception e) {
            model.addAttribute("error", "IP 주소를 가져오는 중 오류가 발생했습니다."); // 에러 처리
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
}
