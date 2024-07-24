import http from '@/http/api';
import { defineStore } from 'pinia';
import { useRoute } from 'vue-router';
import { ref, reactive, computed, watch } from 'vue';
import { useI18n } from 'vue-i18n';

interface GroupTableType {
  resourceCode: string,
  resourceName: string,
  resourceType: string,
  groupId: number;
  groupName: string;
  groupDesc: string;
  expiredAtDisplay: string;
  joinedTime: number;
  expiredAt: number,
  operateSource: string;
  operator: string;
  removeMemberButtonControl: 'OTHER' | 'TEMPLATE' | 'UNIQUE_MANAGER' | 'UNIQUE_OWNER';
};
interface Pagination {
  limit: number;
  current: number;
  count: number;
}
interface SourceType {
  pagination?: Pagination;
  count?: number;
  isAll?: boolean;
  remainingCount?: number;
  resourceTypeName?: string;
  resourceType: string,
  hasNext?: boolean,
  activeFlag?: boolean;
  tableLoading?: boolean;
  scrollLoading?: boolean;
  tableData: GroupTableType[];
}
interface SelectedDataType {
  [key: string]: GroupTableType[];
}
interface CollapseListType {
  resourceType: string;
  resourceTypeName: string;
  count: number;
}
interface AsideItem {
  id: string,
  name: string,
  type: string
}

interface ManageAsideType {
  id: string,
  name: string,
  type: string
};

export default defineStore('userGroupTable', () => {
  const { t } = useI18n();
  const route = useRoute();
  const isLoading = ref(true);

  const projectId = computed(() => route.params?.projectCode as string);
  const paginations = ref({
    'project': [0, 10],
    'pipeline': [0, 10]
  })

  const sourceList = ref<SourceType[]>([]);
  const collapseList = ref<CollapseListType[]>([]);
  const memberItem = ref<ManageAsideType>();

  const isShowRenewal = ref(false);
  const isShowHandover = ref(false);
  const isShowRemove = ref(false);
  const selectedData = reactive<SelectedDataType>({});
  const selectSourceList = ref<SourceType[]>([]);
  const selectedRow = ref<GroupTableType | null>(null);
  const rowIndex = ref<number>();
  const selectedTableGroupType = ref('');
  const selectedLength = ref(0);
  const isPermission = ref(true);
  let currentRequestId = 0;

  watch(selectedData, ()=>{
    getSourceList()
  })

  /**
   * 初始化数据
   */
  function initData() {
    selectedRow.value = null;
    rowIndex.value = undefined;
    selectedTableGroupType.value = '';
    sourceList.value = [];
    selectSourceList.value = [];
    selectedLength.value = 0;
    Object.keys(selectedData).forEach(key => {
      delete selectedData[key];
    });
    paginations.value = {
      'project': [0, 10],
      'pipeline': [0, 10]
    };
  }
  /**
   * 获取项目成员有权限的用户组数量
   */
  async function getCollapseList(memberId: string) {
    try {
      const res = await http.getMemberGroups(projectId.value, memberId);
      collapseList.value = res;
      if(res.length){
        isPermission.value = true;
      } else {
        isPermission.value = false;
      }
      sourceList.value = collapseList.value.map((item) => ({
        ...item,
        tableLoading: false,
        scrollLoading: false,
        tableData: [],
      }))
    } catch (error) {
      console.log(error);
    }
  }
  /**
   * 获取项目成员有权限的用户组
   * @param resourceType 资源类型
   */
  async function getGroupList(resourceType: string) {
    if (!collapseList.value.some(item => item.resourceType === resourceType)) {
      return {};
    }
    try {
      const params = {
        projectId: projectId.value,
        resourceType,
        memberId: memberItem.value!.id,
        start: paginations.value[resourceType][0],
        limit: paginations.value[resourceType][1],
      }
      return await http.getMemberGroupsDetails(params);
    } catch (error) {
      console.log(error);
    }
  }
  /**
   * 获取项目成员页面数据
   */
  async function fetchUserGroupList(asideItem: AsideItem) {
    const requestId = ++currentRequestId;
    initData();
    asideItem && await getCollapseList(asideItem.id);
    memberItem.value = asideItem;
    try {
      isLoading.value = true;
      const resourceTypes = ['project', 'pipeline'];
      const results = await Promise.all(
        resourceTypes.map(resourceType => getGroupList(resourceType))
      );
      const [projectResult, pipelineGroupResult] = results;

      if(currentRequestId === requestId) {
        sourceList.value.forEach(item => {
          if(item.resourceType === "project" && projectResult) {
            item.tableData = projectResult.records;
          }
          if(item.resourceType === "pipeline" && pipelineGroupResult) {
            item.tableData = pipelineGroupResult.records;
            item.count && (item.activeFlag = true);
          }
        })
        isLoading.value = false;
      }
    } catch (error: any) {
      isLoading.value = false;
      console.error(error);
    }
  }
  /**
   * 续期按钮点击
   * @param row 行数据
   */
  function handleRenewal(row: GroupTableType, resourceType: string) {
    selectedRow.value = row;
    selectedTableGroupType.value = resourceType;
    isShowRenewal.value = true;
  }
  /**
   * 移交按钮点击
   * @param row 行数据
   */
  function handleHandOver(row: GroupTableType, resourceType: string, index: number) {
    selectedRow.value = row;
    rowIndex.value = index;
    selectedTableGroupType.value = resourceType;
    isShowHandover.value = true;
  }
  /**
   * 移出按钮点击
   * @param row 行数据
   */
  function handleRemove(row: GroupTableType, resourceType: string, index: number) {
    selectedRow.value = row;
    rowIndex.value = index;
    selectedTableGroupType.value = resourceType;
    isShowRemove.value = true;
  }
  /**
   * 更新表格行数据
   * @param expiredAt 续期时间
   */
  async function handleUpDateRow(expiredAt: number) {
    const activeTable = sourceList.value.find(group => group.resourceType === selectedTableGroupType.value);
    if (activeTable) {
      try {
        const params = {
          groupId: selectedRow.value!.groupId,
          targetMember: memberItem.value,
          renewalDuration: expiredAt
        };
        const res = await http.renewal(projectId.value, params)
        activeTable.tableData = activeTable.tableData.map(item => item.groupId === selectedRow.value!.groupId ? res : item)
      } catch (error) {
        console.log(error);
      }
    }
  }
  /**
   * 删除行数据
   */
  function handleRemoveRow() {
    const current = paginations.value[selectedTableGroupType.value];
    current[2] = (current[2] ?? 0) + 1;

    const activeTableData = sourceList.value.find(group => group.resourceType === selectedTableGroupType.value);
    if (activeTableData) {
      activeTableData.tableData?.splice(rowIndex.value as number, 1);
      activeTableData.tableData = [...activeTableData.tableData];
      activeTableData.count = activeTableData.count! - 1;
    }
  }
  /**
   * 获取表格选择的数据
   */
  function getSelectList(selections: GroupTableType[], resourceType: string) {
    let item = sourceList.value.find((item: SourceType) => item.resourceType == resourceType);
    item && (item.isAll = false);
    selectedData[resourceType] = selections
    if (!selectedData[resourceType].length) {
      delete selectedData[resourceType]
    }
  }
  /**
   * 获取选中的用户组数据
   */
  function getSourceList() {
    selectedLength.value = 0;
    selectSourceList.value = Object.entries(selectedData)
      .map(([key, tableData]: [string, GroupTableType[]]) => {
        const sourceItem = sourceList.value.find((item: SourceType) => item.resourceType == key) as SourceType;
        selectedLength.value += sourceItem.isAll ? sourceItem.count! : tableData.length 
        return {
          pagination: {
            limit: 10,
            current: 1,
            count: sourceItem.isAll ? sourceItem.count! : tableData.length 
          },
          ...sourceItem,
          tableData: tableData.slice(0,11),
          ...(!sourceItem.isAll && { groupIds: tableData.map(item => item.groupId) })
        };
      });
  }
  /**
   * 加载更多
   */
  async function handleLoadMore(resourceType: string) {
    const pagination = paginations.value[resourceType];
    const currentOffset = pagination[0];
    const nextOffsetAdjustment = pagination[2] || 0;

    const newOffset = currentOffset + 10 - nextOffsetAdjustment;
    pagination[0] = newOffset;

    let item = sourceList.value.find((item: SourceType) => item.resourceType == resourceType);
    if(item){
      item.scrollLoading = true;
      const res = await getGroupList(resourceType);
      item.scrollLoading = false;
      item.tableData.push(...res.records);
      if(pagination[2]){
        pagination.pop();
      }
    }
  }
  /**
   * 全量数据选择
   */
  function handleSelectAllData(resourceType: string) {
    let item = sourceList.value.find((item: SourceType) => item.resourceType == resourceType);
    if(item){
      item.isAll = true;
      selectedData[resourceType] = item.tableData
    }
  }
  /**
   * 清除选择
   */
  function handleClear(resourceType: string) {
    let item = sourceList.value.find((item: SourceType) => item.resourceType == resourceType);
    if(item){
      item.isAll = false;
    }
    delete selectedData[resourceType]
  }
  /**
   * 折叠面板调用接口获取表格数据
   */
  async function collapseClick(resourceType: string) {
    let item = sourceList.value.find((item: SourceType) => item.resourceType == resourceType);
    if(item){
      if (!item.count || item.tableData.length) {
        return;
      } else {
        item.activeFlag = true;
        try {
          item.tableLoading = true;
          paginations.value[resourceType] = [0, 10]
          const res = await getGroupList(resourceType);
          item.tableLoading = false;
          item.tableData = res.records;
        } catch (e) {
          console.error(e)
        }
      }
    }
  }
  async function pageLimitChange(limit: number, resourceType: string) {
    paginations.value[resourceType][1] = limit;
    try {
      let item = selectSourceList.value.find((item: SourceType) => item.resourceType == resourceType);
      if(item){
        item.tableLoading = true;
        const res = await getGroupList(resourceType)
        item.tableLoading = false;
        item.tableData = res.records;
      }
    } catch (error) {
      
    }
  }
  async function pageValueChange(value: number, resourceType: string) {
    paginations.value[resourceType][0] = (value - 1) * 10 + 1;
    try {
      let item = selectSourceList.value.find((item: SourceType) => item.resourceType == resourceType);
      if(item){
        item.tableLoading = true;
        const res = await getGroupList(resourceType)
        item.tableLoading = false;
        item.tableData = res.records;
      }
    } catch (error) {
      
    }
  }

  return {
    isLoading,
    sourceList,
    collapseList,
    isShowRenewal,
    isShowHandover,
    isShowRemove,
    selectedData,
    selectedLength,
    selectSourceList,
    selectedRow,
    isPermission,
    fetchUserGroupList,
    handleRenewal,
    handleHandOver,
    handleRemove,
    getSelectList,
    getSourceList,
    handleLoadMore,
    handleSelectAllData,
    handleClear,
    collapseClick,
    handleRemoveRow,
    handleUpDateRow,
    pageLimitChange,
    pageValueChange,
  };
});