enum UserStatus {
  Admin = 1,
  Editor = 2,
  User = 3,
}

function checkStatus(status: number) {
  switch (status) {
    case UserStatus.Admin:
      console.log('É Admin')
      break;
    
    case UserStatus.Editor:
      console.log('É Editor');
      break;
    
    case UserStatus.User:
      console.log('É user normal');
      break;
  }
}