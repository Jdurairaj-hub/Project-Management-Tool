const authToken = localStorage.getItem('authToken');
if (!authToken) {
  return <Redirect to="/login" />;
}