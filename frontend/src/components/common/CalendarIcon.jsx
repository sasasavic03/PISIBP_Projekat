import { useEffect, useState } from "react";
import "./calendaricon.css";

function CalendarIcon({ className = "", value, onChange }) {
  const currentYear = new Date().getFullYear();

  const [year, setYear] = useState("");
  const [month, setMonth] = useState("");
  const [day, setDay] = useState("");

  useEffect(() => {
    if (value) {
      const date = new Date(value);
      setYear(date.getFullYear());
      setMonth(date.getMonth() + 1);
      setDay(date.getDate());
    }
  }, [value]);

  const isLeapYear = (y) => {
    return (y % 4 === 0 && y % 100 !== 0) || y % 400 === 0;
  };

  const getDaysInMonth = (y, m) => {
    if (!m) return 31;

    const days = [31, isLeapYear(y) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    return days[m - 1];
  };

  const daysInMonth = getDaysInMonth(year, month);

  const years = [];
  for (let y = currentYear; y >= 1900; y--) {
    years.push(y);
  }

  const months = [
    "January","February","March","April","May","June",
    "July","August","September","October","November","December"
  ];

  const days = [];
  for (let d = 1; d <= daysInMonth; d++) {
    days.push(d);
  }

  useEffect(() => {
    if (year && month && day) {
      const date = new Date(year, month - 1, day);
      onChange(date);
    }
  }, [year, month, day, onChange]);

  return (
    <div className={`ig-calendar ${className}`}>
      
      <select value={year} onChange={(e) => setYear(Number(e.target.value))}>
        <option value="">Year</option>
        {years.map((y) => (
          <option key={y} value={y}>{y}</option>
        ))}
      </select>

      <select value={month} onChange={(e) => setMonth(Number(e.target.value))}>
        <option value="">Month</option>
        {months.map((m, i) => (
          <option key={m} value={i + 1}>{m}</option>
        ))}
      </select>

      <select value={day} onChange={(e) => setDay(Number(e.target.value))}>
        <option value="">Day</option>
        {days.map((d) => (
          <option key={d} value={d}>{d}</option>
        ))}
      </select>

    </div>
  );
}

export default CalendarIcon;