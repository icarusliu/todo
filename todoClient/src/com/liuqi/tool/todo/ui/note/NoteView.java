package com.liuqi.tool.todo.ui.note;/**
 * Created by icaru on 2017/8/24.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Note;
import com.liuqi.learn.model.User;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import com.liuqi.tool.todo.util.Cache;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/24 9:43
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/24 9:43
 **/
public class NoteView extends BorderPane {
    private TreeView<Note> treeView;

    private NewDirectoryDialog newDialog;

    private TabPane rightPane;
    private Map<Note, Tab> tabMap;

    private Logger logger = LoggerFactory.getLogger(NoteView.class);

    public NoteView() {
        this.treeView = new TreeView<>();
        this.rightPane = new TabPane();
        this.tabMap = new HashMap<>();

        initTreeView();

        initRightPane();

        this.setLeft(treeView);
        this.setCenter(rightPane);

        refreshTree();
    }

    /**
     * 初始化右侧面板
     */
    private final void initRightPane() {
         //选择TAB时，树控件选择的ITEM同时变化
        rightPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null == newValue) {
                return;
            }

            TreeItem<Note> treeItem = (TreeItem<Note>) newValue.getUserData();
            if (null == treeItem) {
                return;
            }

            treeView.getSelectionModel().select(treeItem);
        });

        //给右侧面板添加弹出菜单
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(createMenuItem("关闭", KeyCode.W, e -> {
                            Tab selTab = rightPane.getSelectionModel().getSelectedItem();
                            if (null == selTab) {
                                return;
                            }
                            rightPane.getTabs().remove(selTab);
                        })
                        , createMenuItem("保存", KeyCode.S, e -> {
                            Tab selTab = rightPane.getSelectionModel().getSelectedItem();
                            if (null == selTab) {
                                return;
                            }
                            //保存快捷键处理
                            if (selTab.getContent() instanceof ShowView) {
                                return;
                            }

                            EditorView view = (EditorView) selTab.getContent();
                            view.save();
                        }));

        //没有打开面板的时候，设置菜单为失效状态
        menu.setOnShown(e -> {
            Tab selTab = rightPane.getSelectionModel().getSelectedItem();
            if (null == selTab) {
                menu.getItems().forEach(a -> a.setDisable(true));
            } else {
                menu.getItems().forEach(a -> a.setDisable(false));
            }
        });

        rightPane.setContextMenu(menu);
    }

    /**
     * 初始化结构树
     */
    private final void initTreeView() {
        treeView.setShowRoot(false);

        TreeItem<Note> root = new TreeItem<>();
        Note rootDir = new Note();
        rootDir.setDirectory(true);
        rootDir.setName("根目录");
        root.setValue(rootDir);

        treeView.setRoot(root);
        treeView.setEditable(false);

        //右键单击时打开文档
        treeView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1 && e.getButton().equals(MouseButton.PRIMARY)) {
                TreeItem<Note> selItem = treeView.getSelectionModel().getSelectedItem();
                if (null == selItem) {
                    return;
                }

                openNote();
            }
        });

        //设置树的弹出菜单
        ContextMenu menu = new ContextMenu();
        MenuItem newDirItem = createMenuItem("新建目录", KeyCode.N, e -> addDirectory());
        MenuItem newFileItem = createMenuItem("新建文件", KeyCode.F, e -> addNote());
        MenuItem editItem = createMenuItem("编辑", KeyCode.E, e -> editNote());
        MenuItem renameItem = createMenuItem("重命名", KeyCode.R, e -> rename());
        MenuItem deleteItem =  createMenuItem("删除", KeyCode.D, e -> delete());
        menu.getItems().addAll(newDirItem, newFileItem, editItem, renameItem, deleteItem);
        treeView.setContextMenu(menu);

        //设置菜单显示时哪些菜单需要设置成失效和有效
        menu.setOnShown(e -> {
            TreeItem<Note> selItem = treeView.getSelectionModel().getSelectedItem();
            User loginUser = Cache.getLoginUser();

            //非管理员只能新增文件，无法新增目录
            if (!loginUser.isAdmin()) {
                newDirItem.setDisable(true);
                newFileItem.setDisable(false);
            }

            if (null == selItem) {
                //如果未选择项目，则删除/重命名/编辑项置成无效
                editItem.setDisable(true);
                renameItem.setDisable(true);
                deleteItem.setDisable(true);

                return;
            }

            Note note = selItem.getValue();

            //如果选择了目录或者文件
            if (note.getDirectory()) {
                //目录时无法进行编辑
                editItem.setDisable(true);

                //只有管理员能删除/重命名目录
               deleteItem.setDisable(!loginUser.isAdmin());
               renameItem.setDisable(!loginUser.isAdmin());
            } else {
                //选择了文件
                //管理员能处理所有菜单
                if (loginUser.isAdmin()) {
                    editItem.setDisable(false);
                    renameItem.setDisable(false);
                    deleteItem.setDisable(false);
                } else {
                    //其它用户只能编辑自己添加的文件
                    User addUser = note.getUser();
                    boolean hasPriority = addUser.getId().equals(loginUser.getId());
                    editItem.setDisable(!hasPriority);
                    renameItem.setDisable(!hasPriority);
                    deleteItem.setDisable(!hasPriority);
                }
            }
        });

        //设置树的编辑方式
        //默认树无法编辑，需要设置成TEXTFIELD的TABLECELL
        //但这样同时存在另外一个问题：双击的时候展开目录和编辑同时会生效，暂未解决。
        treeView.setCellFactory(param -> {
            TextFieldTreeCell<Note> cell = new TextFieldTreeCell<Note>() {
                @Override
                public void updateItem(Note item, boolean empty) {
                    super.updateItem(item, empty);
                }
            };

            cell.setConverter(new StringConverter<Note>() {
                @Override
                public String toString(Note object) {
                    cell.setUserData(object);
                    return object.getName();
                }

                @Override
                public Note fromString(String string) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(cell.getParent().toString());
                    }
                    Note note = (Note)cell.getUserData();
                    note.setName(string);

                    return note;
                }
            });

            return cell;
        });

        treeView.setOnEditCommit(e -> {
            Note note = e.getNewValue();
            try {
                AjaxProxy.saveNote(note);
                treeView.setEditable(false);
            } catch (TodoException e1) {
                logger.error("Save failed!", e1);

                AlertProxy.showErrorAlert("保存失败，错误信息：" + e1.getMessage());
            }
        });

        treeView.setOnEditCancel(e -> treeView.setEditable(false));
    }

    /**
     * 新增弹出菜单项
     *
     * @param name
     * @param code
     * @return
     */
    private MenuItem createMenuItem(String name, KeyCode code, EventHandler<ActionEvent> eventHandler) {
        MenuItem item = new MenuItem(name);
        item.setAccelerator(new KeyCodeCombination(code, KeyCombination.CONTROL_DOWN));
        item.setOnAction(eventHandler);

        return item;
    }

    /**
     * 打开文档
     */
    private final void openNote() {
        TreeItem<Note> selItem = treeView.getSelectionModel().getSelectedItem();
        if (null != selItem && !selItem.getValue().getDirectory()) {
            Tab newTab = tabMap.get(selItem.getValue());

            if (null == newTab || !rightPane.getTabs().contains(newTab)) {
                //如果没有显示或者显示了被关闭了
                newTab = new Tab();
                newTab.setUserData(selItem);
                ShowView view = new ShowView(selItem.getValue());
                newTab.setContent(view);

                newTab.setText(selItem.getValue().getName());
                tabMap.put(selItem.getValue(), newTab);

                rightPane.getTabs().add(newTab);
            }

            rightPane.getSelectionModel().select(newTab);
        }
    }

    /**
     * 删除目录或者文件
     */
    private final void delete() {
        TreeItem<Note> selItem = treeView.getSelectionModel().getSelectedItem();
        if (null == selItem) {
            return;
        }

        AlertProxy.showConfirm("确认删除？", e -> {
            //先删除服务器端信息
            if (null != selItem.getValue()) {
                Note note = selItem.getValue();
                try {
                    AjaxProxy.deleteNote(note);

                    //删除树上的节点
                    TreeItem<Note> parent = selItem.getParent();
                    parent.getChildren().remove(selItem);

                    //上级节点的Note中将被删除的子节点删除
                    Note parentNote = parent.getValue();
                    if (null != parentNote && null != parentNote.getChildrens() && parentNote.getChildrens().contains(note)) {
                        parentNote.getChildrens().remove(note);
                    }

                    //如果已经打开，将打开的面板关闭
                    Tab pTab = tabMap.get(note);
                    rightPane.getTabs().remove(pTab);
                    tabMap.remove(note);
                } catch (TodoException e1) {
                    logger.error("Delete failed!", e1);

                    AlertProxy.showErrorAlert("删除");
                }
            }

            return null;
        });
    }

    /**
     * 重命名文件或者目录
     */
    private final void rename() {
        TreeItem<Note> selItem = treeView.getSelectionModel().getSelectedItem();
        if (null == selItem || selItem.equals(treeView.getRoot())) {
            //如果未选择或者选择的是根节点，不能进行重命名处理
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(selItem.toString());
        }

        treeView.setEditable(true);
        treeView.edit(selItem);
    }

    /**
     * 编辑文档
     */
    private final void editNote() {
        TreeItem<Note> selItem = treeView.getSelectionModel().getSelectedItem();
        if (null == selItem || selItem.getValue().getDirectory()) {
            //未选择或者选择的是目录时，不能编辑
            return;
        }

        //编辑文档需要：1 将打开的查看TAB关闭 2 如果已经打开编辑的页面，则进行展示，防止重复打开
        Note note = selItem.getValue();
        Tab tab = tabMap.get(note);
        if (null != tab) {
            Node node = tab.getContent();
            if (node instanceof EditorView) {
                //如果已经打开的是编辑面板，将其进行展示
                rightPane.getSelectionModel().select(tab);
                return;
            }
        }

        //如果没有展示或者展示的是查看面板
        if (null == tab) {
            tab = new Tab();
            tab.setText(note.getName());
            tab.setUserData(selItem);
            tabMap.put(note, tab);
            rightPane.getTabs().add(tab);
            rightPane.getSelectionModel().select(tab);
        }

        EditorView editorView = new EditorView();
        editorView.setItem(note);
        tab.setContent(editorView);
    }

    /**
     * 添加文档
     * 调用服务器接口添加文档，并新增树节点
     */
    private final void addNote() {
        TreeItem<Note> selItem = treeView.getSelectionModel().getSelectedItem();
        if (null == selItem) {
            //如果没有选择目录，则增加到根目录下
            selItem = treeView.getRoot();
        } else if (!selItem.getValue().getDirectory()) {
            //选择的不是目录而是文件，则将新增加的增加到选择文件所在的目录下去
            selItem = selItem.getParent();
        }

        final TreeItem<Note> pSelItem = selItem;

        EditorView view = new EditorView();
        Tab newTab = new Tab();
        newTab.setText("未保存");
        newTab.setContent(view);
        view.setParentTreeItem(selItem);
        view.setSaveCallback(item -> {
            //新增树节点
            TreeItem<Note> treeItem = new TreeItem<>();
            treeItem.setValue(item);
            pSelItem.getChildren().add(treeItem);
            setIcon(treeItem);

            //替换面板的显示标题
            newTab.setText(item.getName());

            //将面板添加到tabMap中去，这样选中树节点时打开的仍旧是编辑页面
            tabMap.put(item, newTab);

            //设置新增的节点被 选中
            treeView.getSelectionModel().select(treeItem);

            return null;
        });

        rightPane.getTabs().add(newTab);
        rightPane.getSelectionModel().select(newTab);
    }

    /**
     * 添加目录
     * 调用服务器接口增加目录并在树中新增节点
     */
    private final void addDirectory() {
        if (null == newDialog) {
            newDialog = new NewDirectoryDialog();
        }

        TreeItem<Note> selItem = treeView.getSelectionModel().getSelectedItem();
        if (null != selItem) {
            //当所有节点都被删除时，选择对象不为空，但实际上ROOT下已经没有节点了
            if (0 == treeView.getRoot().getChildren().size()) {
                selItem = null;
                newDialog.setParent(null);
            } else {
                Note selNote = selItem.getValue();
                if (selNote.getDirectory()) {
                    newDialog.setParent(selItem);
                } else {
                    //不是目录，则将其添加到其上级目录的子目录
                    newDialog.setParent(selItem.getParent());
                }
            }
        } else {
            newDialog.setParent(null);
        }

        final TreeItem<Note> pSelItem = selItem;

        Optional<Note> result = newDialog.showAndWait();
        result.ifPresent(e -> {
            if (null == e) {
                return;
            }

            TreeItem<Note> item = new TreeItem<>();
            item.setValue(e);
            if (null != pSelItem) {
                pSelItem.getChildren().add(item);
            } else {
                treeView.getRoot().getChildren().add(item);
            }

            setIcon(item);
        });
    }

    /**
     * 从服务器获取文档数据并刷新树
     *
     */
    private final void refreshTree() {
        List<Note> list;

        try {
            list = AjaxProxy.findAllNotes();
        } catch (TodoException e) {
            logger.error("Get notes failed, error message: " + e.getMessage(), e);
            return;
        }

        list.sort(Note::compareTo);

        final TreeItem<Note> root = treeView.getRoot();
        root.getChildren().clear();
        list.parallelStream().filter(e -> null == e.getParent())
                .forEach(e ->{
                    if (null != root.getChildren()) {
                        root.getChildren().add(compose(e));
                    }
                });
    }

    /**
     * 根据文档生成树节点
     *
     * @param note
     * @return
     */
    private final TreeItem<Note> compose(Note note) {
        TreeItem<Note> item = new TreeItem<>();
        item.setValue(note);

        List<Note> list = note.getChildrens();
        if (null != list) {
            list.sort(Note::compareTo);
            list.forEach(e -> item.getChildren().add(compose(e)));
        }

        item.expandedProperty().addListener((a, b, c) -> {
            setIcon(item);
        });

        setIcon(item);

        return item;
    }

    /**
     * 根据节点类型设置节点图标
     *
     * @param item
     */
    private void setIcon(TreeItem<Note> item) {
        if (null == item || null == item.getValue()) {
            return;
        }

        //设置图标
        String fileName = "folder.png";
        if (!item.getValue().getDirectory()) {
            fileName = "text_enriched.png";
        } else if (item.isExpanded()) {
            fileName = "open_folder.png";
        }

        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream(new File(fileName))));
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            item.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
