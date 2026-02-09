package valentines.demo.api;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MyApi {

    // Define your 3 GIF paths (these will be served from static/images/)
    private static final String[] GIFS = {
            "/images/shocked-miss-j-ixelp1ngxcjudj64.gif",  // 1st "No"
            "/images/giphy.gif",                             // 2nd "No"
            "/images/shocked-surprised.gif"                  // 3rd "No"
    };

    private static final String[] MESSAGES = {
            "Are you sure?",                                 // 1st "No"
            "Really? Please reconsider! 🥺",                // 2nd "No"
            "My heart is breaking... 💔"                    // 3rd "No"
    };

    @GetMapping("/")
    public String landing() {
        return "index";
    }

    @PostMapping("/landing-yes")
    public String landingYes(HttpSession session) {
        // Clear session when they say yes
        session.removeAttribute("sureCount");
        return "celebration";
    }

    @GetMapping("/countdown")
    public String countdown() {
        return "countdown";
    }

    @PostMapping("/landing-no")
    public String landingNo(HttpSession session) {
        // Start at 0 (will show first GIF/message)
        session.setAttribute("sureCount", 0);
        return "redirect:/sure";
    }

    @GetMapping("/sure")
    public String surePage(HttpSession session, Model model) {
        Integer count = (Integer) session.getAttribute("sureCount");
        if (count == null) count = 0;

        // Determine which GIF and message to show
        int gifIndex = Math.min(count, GIFS.length - 1);  // Don't go beyond array

        model.addAttribute("gifUrl", GIFS[gifIndex]);
        model.addAttribute("sureText", MESSAGES[gifIndex]);
        model.addAttribute("noCount", count);

        return "sure";
    }

    @PostMapping("/sure-yes")
    public String sureYes(HttpSession session) {
        // "Yes, I'm sure I don't want to be your valentine" → show next GIF with more "sures"
        Integer count = (Integer) session.getAttribute("sureCount");
        if (count == null) count = 0;
        count++;

        session.setAttribute("sureCount", count);
        return "redirect:/sure";
    }

    @PostMapping("/sure-no")
    public String sureNo(HttpSession session) {
        // "No, I'm not sure" → Give in and go back to the main question
        session.removeAttribute("sureCount");
        return "redirect:/";
    }
}