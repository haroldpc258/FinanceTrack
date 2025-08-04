package edu.sena.finance.track.controllers;

import edu.sena.finance.track.entities.User;
import edu.sena.finance.track.services.CompanyService;
import edu.sena.finance.track.services.DashboardService;
import edu.sena.finance.track.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.util.Map;

@Controller
public class DashboardController {

    private final UserService userService;
    private final DashboardService dashboardService;
    private final CompanyService companyService;

    @Autowired
    public DashboardController(DashboardService dashboardService, UserService userService, CompanyService companyService) {
        this.dashboardService = dashboardService;
        this.userService = userService;
        this.companyService = companyService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal OidcUser principal) {

        if (userService.isSuperUser(principal)) {
            addAdminAttributes(model, principal);
            return "admin/dashboard";
        }

        addUserAttributes(model, principal);
        return "user/dashboard";
    }

    private void addAdminAttributes(Model model, @AuthenticationPrincipal OidcUser principal) {

        User sub = userService.get(principal);
        Map<String, Object> dashboardData = dashboardService.getDashboardData();

        model.addAttribute("sub", sub);
        model.addAttribute("totalCompanies", dashboardData.get("totalCompanies"));
        model.addAttribute("companiesThisMonth", dashboardData.get("companiesThisMonth"));
        model.addAttribute("totalUsers", dashboardData.get("totalUsers"));
        model.addAttribute("usersThisMonth", dashboardData.get("usersThisMonth"));
        model.addAttribute("totalMovements", dashboardData.get("totalMovements"));
        model.addAttribute("movementsThisMonth", dashboardData.get("movementsThisMonth"));
        model.addAttribute("activityPercentageChange", dashboardData.get("activityPercentageChange"));
        model.addAttribute("activityLevel", dashboardData.get("activityLevel"));
        model.addAttribute("recentCompanies", dashboardData.get("recentCompanies"));
        model.addAttribute("recentMovements", dashboardData.get("recentMovements"));
    }

    private void addUserAttributes(Model model, @AuthenticationPrincipal OidcUser principal) {

        User sub = userService.get(principal);
        Long companyId = sub.getCompany().getId();
        model.addAttribute("sub", sub);
        model.addAttribute("companyId", companyId);

        Map<String, Object> dashboardData = dashboardService.getCompanyDashboardData(companyId);

        // Formateador para valores monetarios
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        DecimalFormat percentFormat = new DecimalFormat("0.0");

        // Preparar datos para las tarjetas estadísticas
        model.addAttribute("totalMovementsThisMoth", dashboardData.get("totalMovementsThisMoth"));
        model.addAttribute("totalRevenue", currencyFormat.format(dashboardData.get("totalRevenue")));
        double revenueChange = (double) dashboardData.get("revenueChange");
        model.addAttribute("revenueChange", (revenueChange >= 0 ? "+" : "") + percentFormat.format(revenueChange) + "% from last month");
        model.addAttribute("revenueTrend", revenueChange >= 0 ? "up" : "down");
        model.addAttribute("revenueTrendColor", revenueChange >= 0 ? "green" : "red");

        model.addAttribute("expenses", currencyFormat.format(dashboardData.get("expenses")));
        double expensesChange = (double) dashboardData.get("expensesChange");
        model.addAttribute("expensesChange", (expensesChange >= 0 ? "+" : "") + percentFormat.format(expensesChange) + "% from last month");
        model.addAttribute("expensesTrend", expensesChange >= 0 ? "up" : "down");
        model.addAttribute("expensesTrendColor", expensesChange >= 0 ? "red" : "green");

        model.addAttribute("activeEmployees", dashboardData.get("activeEmployees"));
        long employeesChange = (long) dashboardData.get("employeesChange");
        model.addAttribute("employeesChange", (employeesChange >= 0 ? "+" : "") + employeesChange + " since last month");
        model.addAttribute("employeesTrend", employeesChange >= 0 ? "up" : "down");
        model.addAttribute("employeesTrendColor", employeesChange >= 0 ? "green" : "red");

        model.addAttribute("profitMargin", percentFormat.format(dashboardData.get("profitMargin")) + "%");
        double profitMarginChange = (double) dashboardData.get("profitMarginChange");
        model.addAttribute("profitMarginChange", (profitMarginChange >= 0 ? "+" : "") + percentFormat.format(profitMarginChange) + "% from last month");
        model.addAttribute("profitMarginTrend", profitMarginChange >= 0 ? "up" : "down");
        model.addAttribute("profitMarginTrendColor", profitMarginChange >= 0 ? "green" : "red");

        // Transacciones recientes
        model.addAttribute("recentMovements", dashboardData.get("recentMovements"));
    }

}
