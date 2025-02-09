package com.chaitany.carbonemissions

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val barChart = findViewById<BarChart>(R.id.barChart)
        val tvTotalEmissions = findViewById<TextView>(R.id.tvTotalEmissions)
        val tvScopeDetails = findViewById<TextView>(R.id.tvScopeDetails)

        val scopeData = readCsvFromAssets()

        if (scopeData.isNotEmpty()) {
            setupPieChart(pieChart, scopeData)
            setupBarChart(barChart, scopeData)
            displayDetails(scopeData, tvTotalEmissions, tvScopeDetails)
        }
    }

    private fun readCsvFromAssets(): Map<String, Float> {
        val scopeTotals = mutableMapOf("Scope 1" to 0f, "Scope 2" to 0f, "Scope 3" to 0f)

        try {
            val inputStream = assets.open("bajaj_auto_emissions.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLine() // Skip the header row

            reader.forEachLine { line ->
                val tokens = line.split(",")
                if (tokens.size >= 4) {
                    val scope = tokens[1].trim()
                    val emission = tokens[3].trim().toFloatOrNull() ?: 0f
                    scopeTotals[scope] = scopeTotals.getOrDefault(scope, 0f) + emission
                }
            }
            reader.close()
        } catch (e: Exception) {
            Log.e("CSV_READ_ERROR", "Error reading CSV: ${e.message}")
        }

        return scopeTotals
    }

    private fun setupPieChart(pieChart: PieChart, scopeData: Map<String, Float>) {
        val entries = scopeData.map { PieEntry(it.value, it.key) }
        val dataSet = PieDataSet(entries, "Carbon Emissions by Scope")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 16f

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    private fun setupBarChart(barChart: BarChart, scopeData: Map<String, Float>) {
        val barEntries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        scopeData.entries.forEachIndexed { index, entry ->
            barEntries.add(BarEntry(index.toFloat(), entry.value))
            labels.add(entry.key)
        }

        val barDataSet = BarDataSet(barEntries, "Carbon Emissions by Scope")
        barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        barDataSet.valueTextSize = 14f

        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()
    }

    private fun displayDetails(scopeData: Map<String, Float>, tvTotalEmissions: TextView, tvScopeDetails: TextView) {
        val totalEmissions = scopeData.values.sum()
        tvTotalEmissions.text = "Total Emissions: $totalEmissions kg CO₂"

        val details = scopeData.entries.joinToString("\n") { "${it.key}: ${it.value} kg CO₂" }
        tvScopeDetails.text = "Detailed Scope Emissions:\n$details"
    }
}
