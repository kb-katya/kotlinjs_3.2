package redux

import data.Lesson
import data.State
import data.Student

fun changeReducer(state: State, action: RAction) =
    when (action) {
        is ChangePresent -> State(
            state.lessons,
            state.students,
            state.presents.mapIndexed { indexLesson, lesson ->
                if (indexLesson == action.lesson)
                    lesson.mapIndexed { indexStudent, student ->
                        if (indexStudent == action.student)
                            !student
                        else student
                    }.toTypedArray()
                else
                    lesson
            }.toTypedArray()
        )
        is CreateStudent -> State(
            state.lessons,
            state.students + Student("Student", "${state.students.size + 1}"),
            state.presents.map {
                it + Array(1) { false }
            }.toTypedArray()
        )
        is CreateLesson -> State(
            state.lessons + Lesson("Lesson ${state.lessons.size + 1}"),
            state.students,
            arrayOf(*state.presents, Array(state.students.size) { false })
        )
        is ChangeStudent -> State(
            state.lessons,
            state.students.mapIndexed { index, student ->
                if (index == action.indexStudent)
                    action.student
                else
                    student
            }.toTypedArray(),
            state.presents
        )
        is ChangeLesson -> State(
            state.lessons.mapIndexed { index, lesson ->
                if (index == action.indexLesson)
                    action.lesson
                else
                    lesson
            }.toTypedArray(),
            state.students,
            state.presents
        )
        is RemoveStudent -> State(
            state.lessons,
            state.students.filterIndexed { i, _ -> i != action.indexStudent }.toTypedArray(),
            state.presents.map {
                it.filterIndexed { i, _ -> i != action.indexStudent }.toTypedArray()
            }.toTypedArray()
        )
        is RemoveLesson -> State(
            state.lessons.filterIndexed { i, _ -> i != action.indexLesson }.toTypedArray(),
            state.students,
            state.presents.filterIndexed { i, _ -> i != action.indexLesson }.toTypedArray()
        )
        else -> state
    }
