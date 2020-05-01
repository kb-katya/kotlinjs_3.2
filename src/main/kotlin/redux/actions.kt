package redux

import data.*

class ChangePresent(val lesson: Int, val student: Int) : RAction

class RemoveStudent(val indexStudent: Int) : RAction

class RemoveLesson(val indexLesson: Int) : RAction

class ChangeStudent(val indexStudent: Int, val student: Student) : RAction

class ChangeLesson(val indexLesson: Int, val lesson: Lesson) : RAction

class CreateStudent : RAction

class CreateLesson : RAction