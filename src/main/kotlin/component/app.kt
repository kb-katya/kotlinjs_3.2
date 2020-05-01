package component

import data.*
import hoc.withDisplayName
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.*
import redux.*

interface AppProps : RProps {
    var store: Store<State, RAction, WrapperAction>
}

interface RouteNumberResult : RProps {
    var number: String
}

fun fApp() =
    functionalComponent<AppProps> { props ->
        val getStore = props.store.getState()

        header {
            h1 { +"App" }
            nav {
                ul {
                    li { navLink("/lessons") { +"Lessons" } }
                    li { navLink("/students") { +"Students" } }
                    li { navLink("/edit_lessons") { +"Edit Lessons" } }
                    li { navLink("/edit_students") { +"Edit Students" } }
                }
            }
        }

        switch {
            route("/lessons",
                exact = true,
                render = {
                    anyList(getStore.lessons, "Lessons", "/lessons")
                }
            )
            route("/students",
                exact = true,
                render = {
                    anyList(getStore.students, "Students", "/students")
                }
            )
            route("/lessons/:number",
                render = renderLesson(props)
            )
            route("/students/:number",
                render = renderStudent(props)
            )
            route("/edit_students",
                exact = true,
                render = {
                    anyEdit(RBuilder::editStudent, RBuilder::student, getStore.students,
                        props.onClickSubmitStudent(), props.onClickRemoveStudent(), props.onClickCreateStudent())
                }
            )
            route("/edit_lessons",
                exact = true,
                render = {
                    anyEdit(RBuilder::editLesson, RBuilder::lesson, getStore.lessons,
                         props.onClickSubmitLesson(), props.onClickRemoveLesson(), props.onClickCreateLesson())
                }
            )
        }
    }

fun AppProps.onClickStudent(num: Int): (Int) -> (Event) -> Unit =
    { index ->
        {
            store.dispatch(ChangePresent(index, num))
        }
    }

fun AppProps.onClickLesson(num: Int): (Int) -> (Event) -> Unit =
    { index ->
        {
            store.dispatch(ChangePresent(num, index))
        }
    }


fun AppProps.onClickSubmitLesson(): (Int) -> (Lesson) -> (Event) -> Unit =
    { index ->
        { lesson ->
            {
                store.dispatch(ChangeLesson(index, lesson))
            }
        }
    }

fun AppProps.onClickSubmitStudent(): (Int) -> (Student) -> (Event) -> Unit =
    { index ->
        { student ->
            {
                store.dispatch(ChangeStudent(index, student))
            }
        }
    }

fun AppProps.onClickRemoveLesson(): (Int) -> (Event) -> Unit =
    { index ->
        {
            store.dispatch(RemoveLesson(index))
        }
    }

fun AppProps.onClickRemoveStudent(): (Int) -> (Event) -> Unit =
    { index ->
        {
            store.dispatch(RemoveStudent(index))
        }
    }

fun AppProps.onClickCreateLesson(): (Event) -> Unit =
    {
        store.dispatch(CreateLesson())
    }

fun AppProps.onClickCreateStudent(): (Event) -> Unit =
    {
        store.dispatch(CreateStudent())
    }

fun RBuilder.renderLesson(props: AppProps) =
    { route_props: RouteResultProps<RouteNumberResult> ->
        val num = route_props.match.params.number.toIntOrNull() ?: -1
        val lesson = props.store.getState().lessons.getOrNull(num)
        if (lesson != null)
            anyFull(
                RBuilder::student,
                lesson,
                props.store.getState().students,
                props.store.getState().presents[num],
                props.onClickLesson(num)
            )
        else
            p { +"No such lesson" }
    }

fun RBuilder.renderStudent(props: AppProps) =
    { route_props: RouteResultProps<RouteNumberResult> ->
        val num = route_props.match.params.number.toIntOrNull() ?: -1
        val student = props.store.getState().students.getOrNull(num)
        if (student != null)
            anyFull(
                RBuilder::lesson,
                student,
                props.store.getState().lessons,
                props.store.getState().presents.map {
                    it[num]
                }.toTypedArray(),
                props.onClickStudent(num)
            )
        else
            p { +"No such student" }
    }


fun RBuilder.app(
    store: Store<State, RAction, WrapperAction>
) =
    child(
        withDisplayName("App", fApp())
    ) {
        attrs.store = store
    }





