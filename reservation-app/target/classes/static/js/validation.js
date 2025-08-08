function validationForm(){
  const name = document.querySelector('[name="name"]').value;
  const email = document.querySelector('[name="email"]').value;
  const phoneNumber = document.querySelector('[name="phoneNumber"]').value;
  const dateInput = document.querySelector('[name="date"]').value;

  const reservationDate = new Date(dateInput);
  const now = new Date();
  now.setSeconds(0, 0); // 秒・m秒を切り捨てて正確に比較


  // 名前チェック
  if(name.length < 2 || name.length > 50){
    alert("名前は2〜50文字で入力してください");
    return false;
  }
  
  // 
  if(!email.includes('@') || !/^[\w.%+-]+@[\w.-]+\.[a-zA-Z]{2,}$/.test(email)){
    alert("メールアドレスの形式が正しくありません");
    return false;
  }

  // 電話番号チェック
  if(!/^\d{10,11}$/.test(phoneNumber)){
    alert("電話番号は10〜11桁の数字で入力してください");
    return false;
  }
  
  // 日付チェック
  if(!dateInput || reservationDate <= now){
    alert("予約日時は未来の日付を選んでください");
    return false;

  }
  
  return true;
}
