// "Move 'return' to computation of the value of 'n'" "true"
class T {
    int f(int a) {
        int n = -1;
        while (true) {
            n = g();
            if(n != 0) return n;
        }
    }

    int g() {
        return 1;
    }
}