private static void runOptimalAllocationByTime(FileWriter writer) throws IOException {
        writer.write("\nOptimal Allocation by Time Horizon:\n");
        writer.write("Years\tBest Stock %\tBest Final Balance\n");

        // Loop over time spans: 1 to 40 years
        for (int years = 1; years <= 40; years++) {
            double bestAvgBalance = 0;
            double bestStockPercent = -1;

            // Try allocations from 100% stocks to 0% stocks in 10% steps
            for (int stockPct = 0; stockPct <= 100; stockPct += 10) {
                double totalBalance = 0;
                int runCount = 0;

                // Loop over all valid start years so (start + years <= 2024)
                for (int startYear = 1985; startYear + years <= 2024; startYear++) {
                    YearMonth start = YearMonth.of(startYear, 5);
                    YearMonth end = YearMonth.of(startYear + years, 5);

                    // Run simulation using real historical data
                    InvestSimulator sim = new InvestSimulator(
                            INITIAL_BALANCE,
                            MONTHLY_CONTRIBUTION,
                            stockPct / 100.0,
                            stockPct / 100.0, // flat allocation
                            FIXED_YIELD,
                            ANNUAL_INCREASE,
                            start,
                            end
                    );
                    sim.runSimulation();
                    totalBalance += sim.getFinalAmount();
                    runCount++;
                }

                if (runCount > 0) {
                    double avgBalance = totalBalance / runCount;

                    // Update best allocation if this one is better
                    if (avgBalance > bestAvgBalance) {
                        bestAvgBalance = avgBalance;
                        bestStockPercent = stockPct;
                    }
                }
            }

            // Output result for this time horizon
            if (bestStockPercent >= 0) {
                writer.write(String.format("%d\t%.0f\t%.2f%n", years, bestStockPercent, bestAvgBalance));
            } else {
                writer.write(String.format("%d\tN/A\tN/A%n", years));
            }
        }
    }
