package appTest.common

import appTest.RunWeb

class SubfilterItems extends RunWeb {

    // Query for testing provided by manual test cases
    private static final queryForSubFilters =
            '''
            use Clarkitbiz;
    SELECT PARENT.id as parent_category_id
    , PARENT.Name as parent_category_name
    , CHILD.id as child_category_id
    , CHILD.Name as child_category_name
    , PRD.item_number
    --, CHILD.[Left_Pointer]
    FROM tblCategory PARENT
            INNER JOIN  tblCategory CHILD
            on CHILD.Last_ID = PARENT.id
            INNER JOIN vw_tblCategoryLink LNK ON LNK.[ID] = CHILD.[ID]
    INNER JOIN tblProducts PRD
    on PRD.Left_Item_Number = LNK.Left_Item_Number
            and PRD.location = 851
    and PRD.hide_item = 'N'
    INNER JOIN	tblVendors	VEN	ON	VEN.Vendor	=	PRD.Vendor
            AND	PRD.Location	=	851
    LEFT JOIN	vw_sold_vendor_cache	VNC
    ON	VNC.Num	=	VEN.Cache_Num
    --INNER JOIN tblCategory CHILD ON CHILD.[Left_Pointer] BETWEEN PARENT.[Left_Pointer] AND	PARENT.[Right_Pointer]
    WHERE PARENT.Last_ID = 0
    and isNull(PRD.[SearchRank],VNC.[SearchRank]) != 0
    and LNK.Left_Item_Number not in
    (
    select Left_Item_Number
    from vw_tblCategoryLink
    group by Left_Item_Number
            having count(id) > 1
    )
    order by PARENT.id
    , CHILD.id
'''

    // Results returned from query
    def static results

    // Number of item configurations to evoke expected functionality
    private int productAmount1
    private int productAmount2
    private int productAmount3

    // Name of columns in query needed to run program
    static final String PARENT_ID = "parent_category_id"
    static final String CHILD_ID = "child_category_id"
    static final String ITEM_NUMBER = "item_number"

    // Parent and child of IDs of each set of items
    private int firstInumParentID = 0
    private int firstInumChildID = 0
    private int secondInumParentID = 0
    private int secondInumChildID = 0

    // Map of three different sets.
    List<Map<String, String>> sameIdMap = []
    List<Map<String, String>> differentIdMap = []
    List<Map<String, String>> thirdIdMap = []

    ArrayList<String> itemsForSubfilterTest = []

    SubfilterItems(int productAmount1, int productAmount2, int productAmount3 = 0) {

        this.productAmount1 = productAmount1
        this.productAmount2 = productAmount2
        this.productAmount3 = productAmount3

        results = DbTools.selectToListOfMaps(queryForSubFilters, [PARENT_ID, CHILD_ID, ITEM_NUMBER])

        getSameIdItems()
        getDifferentIdItems()
        if (productAmount3 > 0) {
            getThirdSetItems()
        }
        logDebug "First group of items info: " + sameIdMap.toPrettyString()
        logDebug "Second group of items info: " + differentIdMap.toPrettyString()
        logDebug "Third group of items info: " + thirdIdMap.toPrettyString()
    }


    def private getSameIdItems() {
        logDebug "Preparing $productAmount1 items that have the same parent ID and same child ID...."
        for (def res in results) {
            def parentId = res.get(PARENT_ID)
            def childId = res.get(CHILD_ID)
            switch (firstInumParentID == parentId && firstInumChildID == childId) {
                case true:
                    sameIdMap << res
                    itemsForSubfilterTest << res.get(ITEM_NUMBER)
                    if (sameIdMap.size() == productAmount1) {
                        logDebug "Found $productAmount1 items that have the same parent ID and same child ID"
                        return sameIdMap
                    }
                    break
                case false:
                    sameIdMap.clear()
                    firstInumParentID = parentId
                    firstInumChildID = childId
                    break
            }
        }
    }

    def private getDifferentIdItems() {
        for (def res in results) {
            def parentId = res.get(PARENT_ID)
            def childId = res.get(CHILD_ID)
            if (firstInumParentID != parentId && firstInumChildID != childId && !itemsForSubfilterTest.contains(res.get(ITEM_NUMBER))) {
                switch (secondInumParentID == parentId && secondInumChildID == childId) {
                    case true:
                        differentIdMap << res
                        itemsForSubfilterTest << res.get(ITEM_NUMBER)
                        if (differentIdMap.size() == productAmount2) {
                            logDebug "Found $productAmount2 items that have the same parent ID and same child ID"
                            return sameIdMap
                        }
                        break
                    case false:
                        differentIdMap.clear()
                        secondInumParentID = parentId
                        secondInumChildID = childId
                        break
                }
            }
        }
    }

    def private getThirdSetItems() {
        for (def res in results) {
            def parentId = res.get(PARENT_ID)
            def childId = res.get(CHILD_ID)
            if (firstInumParentID != parentId && firstInumChildID != childId && !itemsForSubfilterTest.contains(res.get(ITEM_NUMBER))) {
                switch (secondInumParentID == parentId && secondInumChildID != childId) {
                    case true:
                        thirdIdMap << res
                        itemsForSubfilterTest << res.get(ITEM_NUMBER)
                        if (thirdIdMap.size() == productAmount3) {
                            logDebug "Found $productAmount3 items that have the same parent ID and same child ID"
                            return thirdIdMap
                        }
                        break
                    case false:
                        thirdIdMap.clear()
                        break
                }
            }
        }
    }

    ArrayList<String> getItemsForSubfilterTest() {
        return itemsForSubfilterTest
    }
/**
 *  (!) DO NOT USE. Deprecated method
 */
    def getItemsForSubFilter(String iNums1, String iNums2, String iNums3 = null) {
        RunWeb r = run()
        def iNumArray1 = []
        def iNumArray2 = []
        def iNumArray3 = []
        def iNumSp = []
        iNumArray1 = DbTools.
                selectToListOfMaps(String.format(queryForSubFilters, iNums1, '821', '54305'), ['item_number'])
        r.logDebug "1st item array: " + iNumArray1
        for (inum in iNumArray1) {
            iNumSp << inum.item_number
        }
        iNumArray2 = DbTools.
                selectToListOfMaps(String.format(queryForSubFilters, iNums2, '51953', '51965'), ['item_number'])
        r.logDebug "2nd item array: " + iNumArray2
        for (inum2 in iNumArray2) {
            iNumSp << inum2.item_number
        }
        if (iNums3 != null) {
            iNumArray3 = DbTools.selectToListOfMaps(String.format(queryForSubFilters, iNums3,
                    '51953', '51971'), ['item_number'])
            r.logDebug "3rd item array: " + iNumArray3
            for (inum3 in iNumArray3) {
                iNumSp << inum3.item_number
            }
        }
        r.logDebug iNumSp
        return iNumSp
    }
}
