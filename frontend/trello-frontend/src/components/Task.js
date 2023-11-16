import "../styles/task.scss";
import { useState } from "react";
import axios from "axios";

export default function Task(props) {
  const { addTask, deleteTask, moveTask, task } = props;

  const [urgencyLevel, setUrgencyLevel] = useState(task.urgency);
  const [collapsed, setCollapsed] = useState(task.isCollapsed);
  const [formAction, setFormAction] = useState("");
  const [assignedMember, setAssignedMember] = useState("");

  function setUrgency(event) {
    setUrgencyLevel(event.target.attributes.urgency.value);
  }

  function handleSubmit(event) {
    event.preventDefault();

    if (formAction === "save") {
      if (collapsed) {
        setCollapsed(false);
      } else {
        const title = event.target.elements.title.value;
      const description = event.target.elements.description.value;
      const dueDateStr = event.target.elements.dueDate.value;

      // Convert dueDateStr to a LocalDate object
      const [year, month, day] = dueDateStr.split("T")[0].split("-");
      const dueDateFormatted = `${year}-${month}-${day}`;
      // Check if dueDate is before the creationDate
      const creationDate = task.creationDate;

      if (dueDateFormatted < creationDate) {
        alert("Due date cannot be before the creation date. Please recheck values.");
        return; // Exit the function and prevent task creation
      }

      let newTask = {
        id: task.id,
        name: title,
        description: description,
        urgency: urgencyLevel,
        status: task.status,
        isCollapsed: true,
        creationDate: task.creationDate,
        userEmail: localStorage.getItem("email"),
        dueDate: dueDateFormatted,
        assignedMember:assignedMember, // Use the converted dueDate
      }

        axios.post("http://localhost:8081/task/create", newTask)
        .then((response) => {
          console.log(response.data);
          alert(response.data);
          addTask(newTask);
          setCollapsed(true);
        })
        .catch((error) => {
          alert("Due data might be before the created date. Please recheck values", error)
          console.error("Error creating task:", error);
        });
      }
    }

    if (formAction === "delete") {
      deleteTask(task.id);
    }
  }

  function handleMoveLeft() {
    let newStatus = "";

    if (task.status === "In Progress") {
      newStatus = "Backlog";
    } else if (task.status === "Done") {
      newStatus = "In Progress";
    }

    if (newStatus !== "") {
      moveTask(task.id, newStatus);
    }
  }

  function handleMoveRight() {
    let newStatus = "";

    if (task.status === "Backlog") {
      newStatus = "In Progress";
    } else if (task.status === "In Progress") {
      newStatus = "Done";
    }

    if (newStatus !== "") {
      moveTask(task.id, newStatus);
    }
  }

  function formatDateString(dateString) {
    const dateObj = new Date(dateString);
    return dateObj.toLocaleDateString(); // Changed to use toLocaleDateString()
  }

  return (
    <div className={`task ${collapsed ? "collapsedTask" : ""}`}>
      <button onClick={handleMoveLeft} className="button moveTask">
        &#171;
      </button>
      <form onSubmit={handleSubmit} className={collapsed ? "collapsed" : ""}>
        <input
          type="text"
          className="title input"
          name="title"
          placeholder="Enter Title"
          disabled={collapsed}
          defaultValue={task.name}
        />
        <textarea
          rows="2"
          className="description input"
          name="description"
          placeholder="Enter Description"
          defaultValue={task.description}
        />
        <p className="date">Created Date: {formatDateString(task.creationDate)}</p>
        <label className="dueDate 
        label-dueDate">
          Due Date: 
          <input
            type="date"
            name="dueDate"
            defaultValue={task.dueDate}
            disabled={collapsed}
            className="input-dueDate"
          />
        </label>
        <label className="memberSelect label-assignMember">
          Assign Member:
          <select
            name="assignedMember"
            value={assignedMember}
            onChange={(e) => setAssignedMember(e.target.value)}
            disabled={collapsed}
            className="select-assignMember"
          >
            <option value="">Select a member</option>
            {/* Replace with your list of workplace members */}
            <option value="member1">Member 1</option>
            <option value="member2">Member 2</option>
            {/* Add more options as needed */}
          </select>
          </label>
        <div className="urgencyLabels">
          
          <label className={`low ${urgencyLevel === "low" ? "selected" : ""}`}>
            <input
              urgency="low"
              onChange={setUrgency}
              type="radio"
              name="urgency"
            />
            low
          </label>
          <label
            className={`medium ${urgencyLevel === "medium" ? "selected" : ""}`}
          >
            <input
              urgency="medium"
              onChange={setUrgency}
              type="radio"
              name="urgency"
            />
            medium
          </label>
          <label
            className={`high ${urgencyLevel === "high" ? "selected" : ""}`}
          >
            <input
              urgency="high"
              onChange={setUrgency}
              type="radio"
              name="urgency"
            />
            high
          </label>
        </div>
        <button
          onClick={() => {
            setFormAction("save");
          }}
          className="button"
        >
          {collapsed ? "Edit" : "Save"}
        </button>
        {collapsed && (
          <button
            onClick={() => {
              setFormAction("delete");
            }}
            className="button delete"
          >
            X
          </button>
        )}
      </form>
      <button onClick={handleMoveRight} className="button moveTask">
        &#187;
      </button>
    </div>
  );
}
