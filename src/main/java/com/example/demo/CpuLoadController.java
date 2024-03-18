import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CpuLoadController { // 클래스 이름 수정

    private boolean cpuLoadRunning = false;

    @GetMapping("/") // 오타 수정
    public String index() {
        return "index";
    }

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
