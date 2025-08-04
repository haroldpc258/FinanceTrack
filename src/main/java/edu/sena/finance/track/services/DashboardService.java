package edu.sena.finance.track.services;

import edu.sena.finance.track.entities.Company;
import edu.sena.finance.track.entities.Movement;
import edu.sena.finance.track.entities.enums.Status;
import edu.sena.finance.track.repositories.CompanyRepository;
import edu.sena.finance.track.repositories.MovementRepository;
import edu.sena.finance.track.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class DashboardService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final MovementRepository movementRepository;

    @Autowired
    public DashboardService(CompanyRepository companyRepository, UserRepository userRepository, MovementRepository movementRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.movementRepository = movementRepository;
    }

    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        // Total Empresas
        long totalCompanies = companyRepository.count();
        long companiesThisMonth = companyRepository.countByCreatedOnAfter(startOfMonth);

        // Total Usuarios
        long totalUsers = userRepository.count();
        long usersThisMonth = userRepository.countByCreatedOnAfter(startOfMonth);

        // Movimientos
        long totalMovements = movementRepository.count();
        long movementsThisMonth = movementRepository.countByCreatedOnAfter(startOfMonth);


        // Actividad del Sistema (basada en movimientos recientes)
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        long totalMovementsThisWeek = movementRepository.countByCreatedOnAfter(oneWeekAgo);
        long totalMovementsPreviousWeek = movementRepository.countByCreatedOnBetween(twoWeeksAgo, oneWeekAgo);

        double activityPercentageChange = 0;
        if (totalMovementsPreviousWeek > 0) {
            activityPercentageChange = ((double) (totalMovementsThisWeek - totalMovementsPreviousWeek) / totalMovementsPreviousWeek) * 100;
        }

        String activityLevel = calculateActivityLevel(activityPercentageChange);

        // Empresas Recientes
        List<Company> recentCompanies = companyRepository.findTop5ByOrderByCreatedOnDesc();

        // Movimientos Recientes
        List<Movement> recentMovements = movementRepository.findTop5ByOrderByCreatedOnDesc();

        // Añadir todos los datos al mapa
        dashboardData.put("totalCompanies", totalCompanies);
        dashboardData.put("companiesThisMonth", companiesThisMonth);
        dashboardData.put("totalUsers", totalUsers);
        dashboardData.put("usersThisMonth", usersThisMonth);
        dashboardData.put("totalMovements", totalMovements);
        dashboardData.put("movementsThisMonth", movementsThisMonth);
        dashboardData.put("activityPercentageChange", activityPercentageChange);
        dashboardData.put("activityLevel", activityLevel);
        dashboardData.put("recentCompanies", recentCompanies);
        dashboardData.put("recentMovements", recentMovements);

        return dashboardData;
    }

    public Map<String, Object> getCompanyDashboardData(Long companyId) {
        Map<String, Object> dashboardData = new HashMap<>();

        // Fechas para comparaciones
        LocalDateTime startOfCurrentMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime startOfPreviousMonth = YearMonth.now().minusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime endOfPreviousMonth = YearMonth.now().atDay(1).atStartOfDay().minusSeconds(1);

        long totalMovementsThisMoth = movementRepository.countByCompanyIdAndCreatedOnAfter(
                companyId, startOfCurrentMonth);

        // Total Revenue (ingresos del mes actual)
        double currentMonthRevenue = movementRepository.sumAmountByTypeAndCompanyIdAndCreatedOnAfter(
                Movement.Type.INCOME, companyId, startOfCurrentMonth);

        // Revenue del mes anterior para comparación
        double previousMonthRevenue = movementRepository.sumAmountByTypeAndCompanyIdAndCreatedOnBetween(
                Movement.Type.INCOME, companyId, startOfPreviousMonth, endOfPreviousMonth);

        // Expenses (gastos del mes actual)
        double currentMonthExpenses = movementRepository.sumAmountByTypeAndCompanyIdAndCreatedOnAfter(
                Movement.Type.EXPENSE, companyId, startOfCurrentMonth);

        // Expenses del mes anterior para comparación
        double previousMonthExpenses = movementRepository.sumAmountByTypeAndCompanyIdAndCreatedOnBetween(
                Movement.Type.EXPENSE, companyId, startOfPreviousMonth, endOfPreviousMonth);

        // Active Employees
        long activeEmployees = userRepository.countByCompanyIdAndStatus(companyId, Status.ACTIVE);

        // Employees del mes anterior para comparación
        long previousMonthEmployees = userRepository.countActiveEmployeesBeforeDate(companyId, endOfPreviousMonth);

        // Profit Margin
        double profitMargin = 0;
        if (currentMonthRevenue > 0) {
            profitMargin = ((currentMonthRevenue - currentMonthExpenses) / currentMonthRevenue) * 100;
        }

        // Profit Margin del mes anterior para comparación
        double previousProfitMargin = 0;
        if (previousMonthRevenue > 0) {
            previousProfitMargin = ((previousMonthRevenue - previousMonthExpenses) / previousMonthRevenue) * 100;
        }

        // Calcular cambios porcentuales
        double revenueChange = calculatePercentageChange(previousMonthRevenue, currentMonthRevenue);
        double expensesChange = calculatePercentageChange(previousMonthExpenses, currentMonthExpenses);
        long employeesChange = activeEmployees - previousMonthEmployees;
        double profitMarginChange = profitMargin - previousProfitMargin;

        // Obtener transacciones recientes
        List<Movement> recentMovements = movementRepository.findByCompanyIdAndCreatedOnAfterOrderByCreatedOnDesc(
                companyId, startOfCurrentMonth);

        // Añadir datos al mapa
        dashboardData.put("totalMovementsThisMoth", totalMovementsThisMoth);
        dashboardData.put("totalRevenue", currentMonthRevenue);
        dashboardData.put("revenueChange", revenueChange);
        dashboardData.put("expenses", currentMonthExpenses);
        dashboardData.put("expensesChange", expensesChange);
        dashboardData.put("activeEmployees", activeEmployees);
        dashboardData.put("employeesChange", employeesChange);
        dashboardData.put("profitMargin", profitMargin);
        dashboardData.put("profitMarginChange", profitMarginChange);
        dashboardData.put("recentMovements", recentMovements);

        return dashboardData;
    }

    private String calculateActivityLevel(double activityPercentageChange) {
        String activityLevel = "Normal";
        if (activityPercentageChange > 10) {
            activityLevel = "Alta";
        } else if (activityPercentageChange < -10) {
            activityLevel = "Baja";
        }
        return activityLevel;
    }

    private double calculatePercentageChange(double oldValue, double newValue) {
        if (oldValue == 0) {
            return newValue > 0 ? 100 : 0;
        }
        return ((newValue - oldValue) / oldValue) * 100;
    }

}
