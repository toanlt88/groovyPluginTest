package appTest.test

class HelperClass {
    void functionTest(){
        verify("this is verify function")
    }

    private void verify(String verifyString){

        List<Integer> b = [1,2,3];

        verify(
                b[0] =1,
                [
                        id     : '59e71351-bfd6-4eac-b873-231152a45f88',
                        title  : "Function check for first items 1",
                        details: [
                                b[0] : 1
                        ]
                ],
                IssueCategory.START_UP
        )

        verify(
                b[1] =2,
                [
                        id     : '59e71351-bfd6-4eac-b873-231152a45f88',
                        title  : "Function check for first items 2",
                        details: [
                                b[1] : 2
                        ]
                ],
                IssueCategory.START_UP
        )

        verify(
                b[2] =3,
                [
                        id     : '59e71351-bfd6-4eac-b873-231152a45f88',
                        title  : "Function check for first items 3",
                        details: [
                                b[2] : 3
                        ]
                ],
                IssueCategory.START_UP
        )
    }
}
