import '../App.css';
import Navigation from "../components/NavigationBar";
import Board from "../components/BoardHead"
import TaskList from "../components/ListofTasks"
/**if importing a component, remember to capitalize its reference otherwise it won't work*/

export default function workspace(){



    return (
        <div>

            <Navigation />

             <Board />

            <div>
                <TaskList />
                
            </div>
        </div>


    )


}