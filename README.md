# approval-system
task approval system

[Postman Collection](https://drive.google.com/file/d/19gxlQETgXTPi8CKJ0J0nCuYFFB3OIjFx/view?usp=sharing)


### Users

| endpoint             | method | Description                                       |
|----------------------|--------|---------------------------------------------------|
| /api/v1/user/signUp  | POST   | Creates new and user and returns jwt token.       |
| /api/v1/user/login   | POST   | Accepts username, password and return jwt token.  |
| /api/v1/user         | GET    | Get all users present in system.                  |

### Task

| endpoint                                                | method | Description                                                                              |
|---------------------------------------------------------|--------|------------------------------------------------------------------------------------------|
| /api/v1/task/?approverIds=2&approverIds=3&approverIds=4 | POST   | Accepts task details in body and three approverIds in request parameters. Creates a task |
| /api/v1/task/approve?taskId=1                           | POST   | Accepts taskId as request parameter, approves the task from login user side              |
| /api/v1/task/comment?taskId=1                           | POST   | Accepts taskId as request parameter, in body it accepts comments from user on a task     |