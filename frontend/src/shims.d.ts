// md-editor-v3类型声明
declare module 'md-editor-v3' {
  import { DefineComponent } from 'vue';
  
  interface MdEditorProps {
    modelValue?: string;
    value?: string;
    editorId?: string;
    theme?: 'light' | 'dark';
    previewTheme?: 'default' | 'github' | 'vuepress';
    codeTheme?: 'github' | 'atom' | 'vs' | 'kimbie' | 'monokai' | 'monokai-sublime' | 'paraiso-light' | 'paraiso-dark' | 'tomorrow' | 'twilight';
    language?: string;
    placeholder?: string;
    disabled?: boolean;
    readOnly?: boolean;
    maxLength?: number;
    autoDetectCode?: boolean;
    showCodeRowNumber?: boolean;
    footers?: string[];
    toolbars?: string[];
    toolbarsExclude?: string[];
    noMermaid?: boolean;
    noKatex?: boolean;
    noPrettier?: boolean;
    noIconfont?: boolean;
    noUpload?: boolean;
    formatCopiedText?: boolean;
    showToolbarName?: boolean;
    tableShape?: [number, number];
    onChange?: (value: string) => void;
    onSave?: (value: string) => void;
    onUploadImg?: (files: File[], callback: (urls: string[]) => void) => void;
    onHtmlChanged?: (html: string) => void;
    onGetCatalog?: (list: any[]) => void;
  }
  
  interface MdPreviewProps {
    modelValue?: string;
    value?: string;
    editorId?: string;
    theme?: 'light' | 'dark';
    previewTheme?: 'default' | 'github' | 'vuepress';
    codeTheme?: 'github' | 'atom' | 'vs' | 'kimbie' | 'monokai' | 'monokai-sublime' | 'paraiso-light' | 'paraiso-dark' | 'tomorrow' | 'twilight';
    language?: string;
    codeStyleReverse?: boolean;
    codeStyleReverseList?: string[];
    showCodeRowNumber?: boolean;
    noMermaid?: boolean;
    noKatex?: boolean;
    noHighlight?: boolean;
    formatCopiedText?: boolean;
    onHtmlChanged?: (html: string) => void;
    onError?: (err: any) => void;
  }
  
  interface MdCatalogProps {
    editorId?: string;
    scrollElement?: string | HTMLElement;
    theme?: 'light' | 'dark';
    offsetTop?: number;
    onClick?: (e: MouseEvent, t: any) => void;
  }
  
  export const MdEditor: DefineComponent<MdEditorProps, any, any>;
  export const MdPreview: DefineComponent<MdPreviewProps, any, any>;
  export const MdCatalog: DefineComponent<MdCatalogProps, any, any>;
}

declare module 'md-editor-v3/lib/style.css';