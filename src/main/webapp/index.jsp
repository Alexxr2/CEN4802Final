<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Calculator</title></head>
<body>
  <h1>Calculator</h1>
  <form action="calculate" method="post">
    <input type="number" name="a" required />
    <select name="op">
      <option>+</option>
      <option>-</option>
      <option>*</option>
      <option>/</option>
    </select>
    <input type="number" name="b" required />
    <button type="submit">=</button>
  </form>
  <h2>Result: ${result}</h2>
</body>
</html>
