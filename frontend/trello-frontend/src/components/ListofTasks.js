import React, { useState, useEffect } from "react";
import "../styles/App.scss";
import StatusLine from "./StatusLine";
import Task from "./Task";
import axios from "axios";



function ListofTasks(){

    const [tasks, setTasks] = useState([]);
    //const [searchTerm, setSearchTerm] = useState("");
    //const [dueFilter, setDueFilter] = useState("");


    useEffect(() => {
        const userEmail = localStorage.getItem("email");
        axios
          .get(`http://localhost:8081/task?userEmail=${userEmail}`)
          .then((response) => {
            // Get the tasks data from the response
            const tasks = response.data;
    
            // Update the state with the retrieved tasks
            setTasks(tasks);
          })
          .catch((error) => {
            console.error("Error loading tasks:", error);
            alert("Error loading tasks:", error);
          });
      }, []);

    const [searchTerm, setSearchTerm] = useState(""); // For search functionality
    const [dueFilter, setDueFilter] = useState("");

    // Filter tasks based on the due date option selected
    function filterTasksByDueDate(task) {
        if (!dueFilter) return true;

        const today = new Date();
        const taskDueDate = new Date(task.dueDate);

        if (dueFilter === "today") {
        return taskDueDate.toDateString() === today.toDateString();
        } else if (dueFilter === "week") {
        const nextWeek = new Date(today);
        nextWeek.setDate(today.getDate() + 7);
        return taskDueDate >= today && taskDueDate <= nextWeek;
        } else if (dueFilter === "overdue") {
        return taskDueDate < today;
        }

        return true;
    }

    // Filter tasks based on search term and due date
    function filteredTasks() {
        return tasks.filter(
        (task) =>
            task.title.toLowerCase().includes(searchTerm.toLowerCase()) &&
            filterTasksByDueDate(task)
        );
    }

    function assignMemberToTask(taskId, member) {
        const updatedTasks = tasks.map((task) => {
          if (task.id === taskId) {
            return {
              ...task,
              assignedMember: member,
            };
          }
          return task;
        });

        setTasks(updatedTasks);
        saveTasksToLocalStorage(updatedTasks);
    }

    let taskList, tasksForStatus;

    if (tasksForStatus) {
        taskList = tasksForStatus.map((task) => {
          return (
            <Task
              addTask={(task) => addTask(task)}
              deleteTask={(id) => deleteTask(id)}
              moveTask={(id, status) => moveTask(id, status)}
              key={task.id}
              task={task}
              assignMemberToTask={(taskId, member) => assignMemberToTask(taskId, member)}
            />
          );
        });
    }




    function addEmptyTask(status) {
        const lastTask = tasks[tasks.length - 1];
        let newTaskId = 1;
      
        if (lastTask !== undefined) {
          newTaskId = lastTask.id + 1;
        }
      
        setTasks((tasks) => [
          ...tasks,
            {
                id: newTaskId,
                title: "",
                description: "",
                urgency: "",
                status: status,
                creationDate: new Date().toISOString(), // Add created date
                dueDate: "", // Initialize the due date as empty
            },
        ]);
    }

    function updateTask(taskID, updatedTask) {
        return axios.put(`http://localhost:8081/task/${taskID}`, updatedTask)
          .then((response) => {
            console.log(response.data);
            return response.data;
          })
          .catch((error) => {
            console.error("Error updating task:", error);
            return Promise.reject(error);
          });
      }

      function changeTaskStatus(taskID, updatedStatus) {
        return axios.put(`http://localhost:8081/task/${taskID}/changeStatus/${updatedStatus}`)
          .then((response) => {
            console.log(response.data);
            return response.data;
          })
          .catch((error) => {
            console.error("Error changing task status:", error);
            return Promise.reject(error);
          });
      }

      function assignMemberToTask(taskID, userEmail) {
        return axios.put(`http://localhost:8081/task/${taskID}/addMember/${userEmail}`)
          .then((response) => {
            console.log(response.data);
            return response.data;
          })
          .catch((error) => {
            console.error("Error assigning member to task:", error);
            return Promise.reject(error);
          });
      }
      
    
    function addTask(taskToAdd) {
        let filteredTasks = tasks.filter((task) => {
          return task.id !== taskToAdd.id;
        });
    
        let newTaskList = [...filteredTasks, taskToAdd];
    
        setTasks(newTaskList);
    
        saveTasksToLocalStorage(newTaskList);
    }
    
    function deleteTask(taskId) {
        let filteredTasks = tasks.filter((task) => {
          return task.id !== taskId;
        });
    
        setTasks(filteredTasks);
    
        saveTasksToLocalStorage(filteredTasks);
    }
    
    function moveTask(id, newStatus) {
        let task = tasks.filter((task) => {
          return task.id === id;
        })[0];
    
        let filteredTasks = tasks.filter((task) => {
          return task.id !== id;
        });
    
        task.status = newStatus;
    
        let newTaskList = [...filteredTasks, task];
    
        setTasks(newTaskList);
    
        saveTasksToLocalStorage(newTaskList);
    }
    
    function saveTasksToLocalStorage(tasks) {
        localStorage.setItem("tasks", JSON.stringify(tasks));
    }
    
    function loadTasksFromLocalStorage() {
        const userEmail = localStorage.getItem("email");
        axios
        .get(`http://localhost:8081/task?userEmail=${userEmail}`)
        .then((response) => {
        // Get the tasks data from the response
        const tasks = response.data;

        // Update the state with the retrieved tasks
        setTasks(tasks);
    })
    .catch((error) => {
      console.error("Error loading tasks:", error);
      alert("Error loading tasks:", error);
    });

    
        if (tasks) {
          setTasks(tasks);
        }
    }

    const getNextWeekRange = () => {
        const today = new Date();
        const startOfWeek = new Date(
          today.setDate(today.getDate() - today.getDay())
        );
        const endOfWeek = new Date(today.setDate(today.getDate() - today.getDay() + 6));
        return { startOfWeek, endOfWeek };
    };
    
    const isTaskDueThisWeek = (task) => {
        const { startOfWeek, endOfWeek } = getNextWeekRange();
        const taskDueDate = new Date(task.dueDate);
        return taskDueDate >= startOfWeek && taskDueDate <= endOfWeek;
    };

    const getStartOfDay = (date) => {
        const startOfDay = new Date(date);
        startOfDay.setHours(0, 0, 0, 0);
        return startOfDay;
    };
    
    const isTaskOverdue = (task) => {
        const today = new Date();
        const startOfToday = getStartOfDay(today);
        const taskDueDate = getStartOfDay(new Date(task.dueDate));
        return taskDueDate < startOfToday;
    };

    const filterTasksByDueDate = (taskList) => {
        if (filterType === null) {
            return taskList;
        }

        switch (filterType) {
        case 'dueToday':
            return taskList.filter((task) => {
                const today = new Date();
                const ReadToday = new Date(today.getTime() - 1 * 24 * 60 * 60 * 1000);
                const taskDueDate = new Date(task.dueDate);
                return taskDueDate.toDateString() === ReadToday.toDateString();
            });
        case 'dueThisWeek':
            return taskList.filter((task) => isTaskDueThisWeek(task));
        case 'overdue':
            return taskList.filter((task) => isTaskOverdue(task));
        default:
            return taskList;
        }
    };

    // Function to handle search input change
    function handleSearchInputChange(event) {
        setSearchQuery(event.target.value);
    }

    // Function to handle filter button click
    function handleFilter(type) {
        setFilterType(type);
    }

    // Function to show all tasks
    function handleShowAllTasks() {
        setFilterType(null);
    }

    function filteredTasks() {
        const tasksFilteredBySearch = tasks.filter(
          (task) =>
            task.title.toLowerCase().includes(searchQuery.toLowerCase())
        );
    
        return filterTasksByDueDate(tasksFilteredBySearch);
    }
      
    function assignMemberToTask(taskId, member) {
        const updatedTasks = tasks.map((task) => {
          if (task.id === taskId) {
            return {
              ...task,
              assignedMember: member,
            };
          }
          return task;
        });
    
        setTasks(updatedTasks);
        saveTasksToLocalStorage(updatedTasks);
    }

    let taskList, tasksForStatus;

    if (tasksForStatus) {
        taskList = tasksForStatus.map((task) => {
          return (
            <Task
              addTask={(task) => addTask(task)}
              deleteTask={(id) => deleteTask(id)}
              moveTask={(id, status) => moveTask(id, status)}
              key={task.id}
              task={task}
              assignMemberToTask={(taskId, member) => assignMemberToTask(taskId, member)}
            />
          );
        });
    }
    
    return(
        
        <div>
            <h1>Task Management</h1>

            {/* Search and Filter section */}
            <div className="searchWrapper">
                <input
                type="text"
                placeholder="Search tasks..."
                value={searchQuery}
                onChange={handleSearchInputChange}
                />
                <div className="filterButtons">
                    <button onClick={() => handleFilter("dueToday")}>Due today</button>
                    <button onClick={() => handleFilter("dueThisWeek")}>Due this week</button>
                    <button onClick={() => handleFilter("overdue")}>Overdue</button>
                    <button onClick={handleShowAllTasks}>Show all tasks</button>
                </div>
            </div>

            <main>
                <section>
                    <StatusLine
                        tasks={tasks.filter((task) => task.status === "Backlog")}
                        addEmptyTask={addEmptyTask}
                        addTask={addTask}
                        deleteTask={deleteTask}
                        moveTask={moveTask}
                        status="Backlog"
                    />
                    <StatusLine
                        tasks={tasks.filter((task) => task.status === "In Progress")}
                        addEmptyTask={addEmptyTask}
                        addTask={addTask}
                        deleteTask={deleteTask}
                        moveTask={moveTask}
                        status="In Progress"
                    />
                    <StatusLine
                        tasks={tasks.filter((task) => task.status === "Done")}
                        addEmptyTask={addEmptyTask}
                        addTask={addTask}
                        deleteTask={deleteTask}
                        moveTask={moveTask}
                        status="Done"
                    />
                </section>
            </main>
        </div>
    )

}

export default ListofTasks;