import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

const CalendarIcon = ({ value, onChange }) => {
  return (
    <DatePicker
      showIcon
      selected={value || null}
      onChange={(date) => onChange(date)}
      dateFormat="dd.MM.yyyy"
    />
  );
};

export default CalendarIcon;