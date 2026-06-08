package com.sky.service;

import java.time.LocalDate;

import com.sky.vo.TurnoverReportVO;

public interface ReportService
{
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
