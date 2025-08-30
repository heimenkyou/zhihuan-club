// Markdown编辑器类型声明
declare module '@kangc/v-md-editor' {
  import { DefineComponent } from 'vue';
  interface VMdEditorProps {
    modelValue?: string;
    value?: string;
    mode?: 'edit' | 'preview';
    height?: string;
    disabledMenus?: string[];
  }
  
  const VMdEditor: DefineComponent<VMdEditorProps, any, any>;
  export default VMdEditor;
  export const githubTheme: any;
}

declare module '@kangc/v-md-editor/lib/theme/github.js' {
  const githubTheme: any;
  export default githubTheme;
}

declare module '@kangc/v-md-editor/lib/style/base-editor.css';
declare module '@kangc/v-md-editor/lib/theme/style/github.css';

// Prism语法高亮类型声明
declare module 'prismjs' {
  const Prism: any;
  export default Prism;
}

declare module 'prismjs/components/prism-json';
declare module 'prismjs/components/prism-javascript';
declare module 'prismjs/components/prism-css';
declare module 'prismjs/components/prism-markup';