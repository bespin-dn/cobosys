package com.example.demo; // package 설정을 하지 않으면, 구조를 불러오지 못해 404 에러 발생.

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import java.net.InetAddress;

@Controller // @RestController로 하니 rendering을 하지 못하는 문제.
public class CpuLoadController { // 클래스 이름 수정

    private boolean cpuLoadRunning = false;

    @GetMapping("/")
    public String index(Model model) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            String ipAddress = ip.getHostAddress();
            model.addAttribute("ipAddress", ipAddress); // IP 주소를 모델에 추가
        } catch (Exception e) {
            model.addAttribute("error", "IP 주소를 가져오는 중 오류가 발생했습니다."); // 에러 처리
        }

        model.addAttribute("cpuLoadRunning", cpuLoadRunning); // cpuLoadRunning 상태 또한 모델에 추가
        return "index";
    }

    // @GetMapping("/") // 오타 수정
    // public String index() {
    //     return "index";
    // }

    @PostMapping("/start-cpu-load")
    public String startCpuLoad() {
        cpuLoadRunning = true;
        new Thread(this::generateCpuLoad).start(); // CPU 부하 생성을 위한 쓰레드 시작
        return "redirect:/"; // 리다이렉트 수정
    }

    @PostMapping("/stop-cpu-load")
    public String stopCpuLoad() {
        cpuLoadRunning = false;
        return "redirect:/"; // 리다이렉트 수정
    }

    private void generateCpuLoad() {
        while (cpuLoadRunning) {
            double x = 0.0001;
            for (int i = 0; i < 1000000; i++) {
                x = Math.sqrt(x);
            }
        }
    }
}