class OperatorAssignment {
    public static void main(String[] args) {
      int a = 10;
      double b = 0.5;

      <caret>a *= b;

      System.out.println(a);
    }
}