import { GetterTree } from 'vuex'
import { isShallowEqual } from '../utils/util'
import { EMPTY_PROJECT } from './constants'

const getters: GetterTree<RootState, any> = {
    getCollectServices: (state: RootState) => {
        return state.services.reduce((collects: any, service: any) => {
            Array.isArray(service.children) && service.children.forEach((child: any) => {
                if (child.collected && child.status !== 'developing' && child.status !== 'planning') {
                    collects.push(child)
                }
            })
            return collects
        }, [])
    },

    isEmptyProject: () => (project: Project): boolean => {
        return isShallowEqual(project, EMPTY_PROJECT)
    },

    enableProjectList: (state: RootState) => {
        return state.projectList.filter((project: ObjectMap) => [2, 4].includes(project.approvalStatus) && project.enabled)
    },

    disableProjectList: (state: RootState) => {
        return state.projectList.filter((project: ObjectMap) => [2, 4].includes(project.approvalStatus) && !project.enabled)
    },

    approvalingProjectList: (state: RootState) => {
        return state.projectList.filter((project: ObjectMap) => project.approvalStatus === 1)
    },

    // 是否显示跑马灯类型公告
    showAnnounce (state: RootState) {
        // @ts-ignore
        return state.currentNotice && state.currentNotice.id && state.currentNotice.noticeType === 1
    }
    
}

export default getters
