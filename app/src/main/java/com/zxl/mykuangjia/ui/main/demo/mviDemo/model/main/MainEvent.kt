package com.zxl.mvidemo3.model.main

interface Event {
    sealed class ReportEvent {
        data class DeleteReportEvent(val message: String) : ReportEvent()
    }
}