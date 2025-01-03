interface ProfileProps {
    className?: string;
}


function Profile(props:ProfileProps) {
  return (
    <div>
      <button type= "button" className={props.className}>Profile</button>
    </div>
  );
}

export default Profile;