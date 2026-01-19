package org.client.chatapp.ui.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class TimeUtils {

    /**
     * Formats a message timestamp like WhatsApp:
     * - Less than 1 hour ago → "5 min ago"
     * - Today → "hh:mm a" (12-hour format with AM/PM)
     * - Yesterday → "Yesterday"
     * - Same week → "Mon", "Tue", etc.
     * - Older → "dd/MM/yyyy"
     */
    public static String formatChatTimestamp(LocalDateTime messageTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate messageDate = messageTime.toLocalDate();
        LocalDate today = now.toLocalDate();
        LocalDate yesterday = today.minusDays(1);

        // Less than 1 hour ago → relative
        Duration diff = Duration.between(messageTime, now);
        if (!messageDate.isBefore(today) && diff.toMinutes() < 60) {
            long minutes = diff.toMinutes();
            return minutes == 0 ? "Just now" : minutes + " min ago";
        }

        // Today → 12-hour format with AM/PM
        if (messageDate.isEqual(today)) {
            return messageTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        }

        // Yesterday
        if (messageDate.isEqual(yesterday)) {
            return "Yesterday";
        }

        // Same week → day of week (Mon, Tue, etc.)
        DayOfWeek day = messageDate.getDayOfWeek();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1); // Monday
        if (!messageDate.isBefore(startOfWeek)) {
            return day.getDisplayName(TextStyle.SHORT, Locale.getDefault());
        }

        // Older → show date
        return messageDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
